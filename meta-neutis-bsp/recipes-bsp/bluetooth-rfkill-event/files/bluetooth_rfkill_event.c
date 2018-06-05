#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>
#include <errno.h>
#include <fcntl.h>
#include <string.h>
#include <sys/poll.h>
#include <sys/time.h>
#include <limits.h>
#include <bluetooth/bluetooth.h>
#include <bluetooth/hci.h>
#include <bluetooth/hci_lib.h>

enum {
    BT_PWR,
    BT_HCI,
};

/* list of all supported chips:
   name is defined in the kernel driver implementing rfkill interface for power */
#define BCM_43341_UART_DEV "/dev/ttyS1"
#define BAUDRATE "1500000"
#define BCM_DEVICE "bcm43xx"

enum rfkill_operation {
    RFKILL_OP_ADD = 0,
    RFKILL_OP_DEL,
    RFKILL_OP_CHANGE,
    RFKILL_OP_CHANGE_ALL,
};

enum rfkill_type {
    RFKILL_TYPE_ALL = 0,
    RFKILL_TYPE_WLAN,
    RFKILL_TYPE_BLUETOOTH,
    RFKILL_TYPE_UWB,
    RFKILL_TYPE_WIMAX,
    RFKILL_TYPE_WWAN,
    RFKILL_TYPE_GPS,
    RFKILL_TYPE_FM,
    NUM_RFKILL_TYPES,
};

struct rfkill_event {
    unsigned int idx;
    unsigned char type;
    unsigned char op;
    unsigned char soft, hard;
} __packed;

/* HCI UART driver initialization utility; usually it takes care of FW patch download as well */
char hciattach[PATH_MAX] = "hciattach";
char hci_uart_default_dev[PATH_MAX] = BCM_43341_UART_DEV;
char hci_bcm_device[PATH_MAX] = BCM_DEVICE;
char hci_baudrate[PATH_MAX] = BAUDRATE;

bool hci_dev_registered;
int bt_pwr_rfkill_idx;

static void free_hci()
{
    char cmd[PATH_MAX];

    snprintf(cmd, sizeof(cmd), "pidof %s", hciattach);

    if (!system(cmd)) {
        snprintf(cmd, sizeof(cmd), "killall %s", hciattach);
        system(cmd);
        printf("killing %s\n", hciattach);
        fflush(stdout);
        hci_dev_registered = false;
    }
}

static void attach_hci()
{
    char hci_execute[PATH_MAX];

    snprintf(hci_execute, sizeof(hci_execute), "%s %s %s %s", hciattach, hci_uart_default_dev, hci_bcm_device, hci_baudrate);

    printf("execute %s\n", hci_execute);
    fflush(stdout);

    system(hci_execute);

    /* remember if hci device has been registered (in case conf file is changed) */
    hci_dev_registered = true;
}

/* calling this routine to be sure to have rfkill hci bluetooth interface unblocked:
   if user does:
   - 'rfkill block bluetooth' and then
   - 'rfkill unblock 2'
   once hci bluetooth is registered back it will be blocked */
static void rfkill_bluetooth_unblock()
{
    int fd, sk, times;
    int ret = -1;
    struct rfkill_event event;

    fd = open("/dev/rfkill", O_RDWR | O_CLOEXEC);
    if (fd < 0) {
        perror("fail to open rfkill interface");
        return;
    }
    memset(&event, 0, sizeof(event));
    event.op = RFKILL_OP_CHANGE_ALL;
    event.type = RFKILL_TYPE_BLUETOOTH;
    event.soft = 0;
    if (write(fd, &event, sizeof(event)) < 0) {
        perror("fail to unblock rfkill bluetooth");
    }
    close(fd);

    printf("Send rfkill unblock event after \n");
    fflush(stdout);
}

static void init_ap6212()
{
    /* if unblock is for power interface: download patch and eventually register hci device */
    free_hci();
    attach_hci();
    /* force to unblock also the bluetooth hci rfkill interface if hci device was registered */
    if (hci_dev_registered) {
        rfkill_bluetooth_unblock();
    }
}

int main(int argc, char** argv)
{
    struct rfkill_event event;
    struct timeval tv;
    struct pollfd p;
    ssize_t len;
    int fd, fd_name, n, type;
    int ret, hci_dev_id;
    char* script;
    char sysname[PATH_MAX];

    /* this code is ispired by rfkill source code */

    fd = open("/dev/rfkill", O_RDONLY);
    if (fd < 0) {
        perror("Can't open RFKILL control device");
        return fd;
    }

    memset(&p, 0, sizeof(p));
    p.fd = fd;
    p.events = POLLIN | POLLHUP;

    while (1) {
        n = poll(&p, 1, -1);
        if (n < 0) {
            perror("Failed to poll RFKILL control device");
            break;
        }

        if (n == 0)
            continue;

        len = read(fd, &event, sizeof(event));
        if (len < 0) {
            perror("Reading of RFKILL events failed");
            break;
        }

        if (len != sizeof(event)) {
            perror("Wrong size of RFKILL event");
            break;
        }

        /* ignore event for others interfaces (not bluetooth) */
        if (event.type != RFKILL_TYPE_BLUETOOTH)
            continue;

        gettimeofday(&tv, NULL);
        printf("%ld.%06u: idx %u type %u op %u soft %u hard %u\n",
            (long)tv.tv_sec, (unsigned int)tv.tv_usec,
            event.idx, event.type, event.op, event.soft, event.hard);
        fflush(stdout);

        /* try to read rfkill interface name only if event is not a remove one, in this case call free_hci */
        if (event.op != RFKILL_OP_DEL) {
            /* get the name to check the bt chip */
            snprintf(sysname, sizeof(sysname), "/sys/class/rfkill/rfkill%u/name", event.idx);

            fd_name = open(sysname, O_RDONLY);
            if (fd_name < 0) {
                perror("fail to open rfkill name");
                continue;
            }

            memset(sysname, 0, sizeof(sysname));

            /* read name */
            if (read(fd_name, sysname, sizeof(sysname) - 1) < 0) {
                perror("fail to read rfkill name");
                close(fd_name);
                continue;
            }

            close(fd_name);

            /* based on chip read its config file, if any, and define the hciattach utility used to dowload the patch */
            if (strstr(sysname, "hci")) {
                type = BT_HCI;

                hci_dev_id = atoi(sysname + 3);
                printf("Event for hci%u interface\n", hci_dev_id);
                fflush(stdout);
            }
            else if (strstr(sysname, "bcm")) {
                type = BT_PWR;
                printf("Event for pwr - %s interface\n", sysname);
                fflush(stdout);
            }
            else {
                printf("Event for unknown intarface, continue...\n");
                fflush(stdout);
                continue;
            }
        }

        switch (event.op) {
        case RFKILL_OP_CHANGE_ALL:
        case RFKILL_OP_CHANGE:
            printf("Change event received\n");
            fflush(stdout);

            if (event.soft == 0 && event.hard == 0) {
                rfkill_bluetooth_unblock();

                if ((type == BT_PWR) || (type == BT_HCI && hci_dev_registered)) {
                    init_ap6212();
                }
            }
            else if (event.soft == 1 && type == BT_PWR) {
                /* for a block event on power interface force unblock of hci device interface */
                free_hci();
            }

            /* save index of rfkill interface for bluetooth power */
            if (event.op == RFKILL_OP_ADD && type == BT_PWR)
                bt_pwr_rfkill_idx = event.idx;
            break;
        case RFKILL_OP_ADD:
            printf("Add event received\n");
            fflush(stdout);

            if (type == BT_HCI && event.soft == 0)
                hci_dev_registered = true;
            if (type == BT_PWR && event.soft == 0)
                init_ap6212();
            break;
        case RFKILL_OP_DEL:
            printf("Delete event received\n");
            fflush(stdout);

            /* in case pwr rfkill interface is removed, unregister hci dev if it was registered */
            if (bt_pwr_rfkill_idx == event.idx && hci_dev_registered)
                free_hci();
            break;
        default:
            continue;
        }
    }

    close(fd);

    return 0;
}
