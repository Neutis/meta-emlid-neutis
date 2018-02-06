/* Copyright (c) 2018 Emlid Ltd, <aleksandr.aleksandrov@emlid.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

#include <stdio.h>
#include <string>
#include <cstring>
#include <fcntl.h>
#include <termios.h>
#include <unistd.h>
#include <sys/time.h>
#include <sys/reboot.h>

#define FEL_MODE            1
#define NORMAL_MODE         0
#define DELAY_MS            3000    /* ms */
#define SERIAL_PORT         "/dev/ttyGS0"
#define BLOCK_DEVICE        "/dev/mmcblk2"
#define MAGIC_WORD          ",emeveryis'sonefrlidomhellome"

int configureSerialPort(void);
int waitForCmd(int fd);
long long gettime_ms(void);
void enterFELMode(void);

int main(void)
{
    int fd, mode;

    fd = configureSerialPort();

    if (fd < 0)
        return -1;

    mode = waitForCmd(fd);
    close(fd);

    switch(mode) {
        case NORMAL_MODE:
            break;
        case FEL_MODE:
            fprintf(stdout, "Got the magic word, go to FEL mode!\n");
            enterFELMode(); break;
        default:
            break;
    }

    return 0;
}

int configureSerialPort(void)
{
    int fd;
    struct termios tty_settings;

    fd = open(SERIAL_PORT, O_RDWR | O_NOCTTY | O_NONBLOCK);

    if (fd < 0) {
        fprintf(stderr, "Unable to open the serial port: %s.\n", SERIAL_PORT);
        return -1;
    }

    if (tcgetattr(fd, &tty_settings) != 0) {
        fprintf(stderr, "Got error during tcgetattr.\n");
        goto fail;
    }

    cfsetospeed (&tty_settings, B9600);
    cfsetispeed (&tty_settings, B9600);

    tty_settings.c_iflag = 0;
    tty_settings.c_oflag = 0;
    tty_settings.c_lflag = 0;
    tty_settings.c_cc[VMIN] = 0;
    tty_settings.c_cc[VTIME] = 0;

    tty_settings.c_cflag &= ~PARENB;                  /* No Parity */
    tty_settings.c_cflag &= ~CSTOPB;                  /* Stop bits = 1 */
    tty_settings.c_cflag |=  CS8;                     /* Set the data bits = 8 */
    tty_settings.c_cflag &= ~CRTSCTS;                 /* No RTS/CTS */
    tty_settings.c_cflag |=  CREAD | CLOCAL;

    if (tcsetattr(fd, TCSANOW, &tty_settings) != 0) {
        fprintf(stderr, "Got error during tcsetattr.\n");
        goto fail;
    }

    tcflush(fd, TCIOFLUSH);
    return fd;

fail:
    close(fd);
    return -1;
}

int waitForCmd(int fd)
{
    std::string output;
    char buff[4096];
    long long start_time;

    start_time = gettime_ms();

    while (gettime_ms() - start_time <= DELAY_MS) {
        if (read(fd, buff, sizeof(buff)) <= 0) {
            usleep(10 * 1000);      /* 10 ms */
        } else {
            output += std::string(buff);
            memset(buff, '\0', sizeof(buff));

            if (output.find(MAGIC_WORD) != std::string::npos)
                return FEL_MODE;
        }
    }

    return NORMAL_MODE;
}

long long gettime_ms(void)
{
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return (((long long)tv.tv_sec) * 1000.0) + (tv.tv_usec / 1000.0);
}

void enterFELMode(void)
{
    std::string dd_cmd;
    FILE* pipe;
    char buff[128];

    dd_cmd.append("/bin/dd if=/dev/zero of=");
    dd_cmd.append(BLOCK_DEVICE);
    dd_cmd.append(" bs=1024 count=40");

    /* Clear the boot part of block device, allwinner won't be able to find
       the magic word and boot, it means that SoC will go to FEL mode */
    pipe = popen(dd_cmd.c_str(), "r");

    if (!pipe) {
        fprintf(stderr, "Unable to clear block device using 'dd'.\n");
        return;
    }

    while (fgets(buff, sizeof(buff), pipe) != NULL)
        fprintf(stdout, "%s", buff);

    pclose(pipe);
    sync();

    /* Force reboot */
    reboot(RB_AUTOBOOT);
}
