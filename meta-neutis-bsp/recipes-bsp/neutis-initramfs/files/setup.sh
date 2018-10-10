read_args() {
    export FSCK_LIST
    export RESIZE_LIST
    [ -z "$CMDLINE" ] && CMDLINE=`cat /proc/cmdline`
    for arg in $CMDLINE; do
        optarg=`expr "x$arg" : 'x[^=]*=\(.*\)'`
        case $arg in
            root=*)
                ROOT_DEVICE=$optarg
                FSCK_LIST="$FSCK_LIST ROOT_DEVICE" ;;
            rootimage=*)
                ROOT_IMAGE=$optarg ;;
            rootfstype=*)
                modprobe $optarg 2> /dev/null ;;
            LABEL=*)
                label=$optarg ;;
            data=*)
                DATA_DEVICE=$optarg
                FSCK_LIST="$FSCK_LIST DATA_DEVICE"
                RESIZE_LIST="$RESIZE_LIST DATA_DEVICE" ;;
            rw)
		        readonly=n ;;
            #fsck.root=yes)
            #    FSCK_LIST="$FSCK_LIST ROOT_DEVICE" ;;
            #fsck.data=yes)
            #    FSCK_LIST="$FSCK_LIST DATA_DEVICE" ;;
            resize.root=yes)
                RESIZE_LIST="$RESIZE_LIST ROOT_DEVICE" ;;
            #resize.data=yes)
            #    RESIZE_LIST="$RESIZE_LIST DATA_DEVICE" ;;
            console=*)
                if [ -z "${console_params}" ]; then
                    console_params=$arg
                else
                    console_params="$console_params $arg"
                fi ;;
            debugshell*)
                if [ -z "$optarg" ]; then
                        shelltimeout=30
                else
                        shelltimeout=$optarg
                fi 
        esac
    done
}

udev_daemon() {
  OPTIONS="/sbin/udev/udevd /sbin/udevd /lib/udev/udevd /lib/systemd/systemd-udevd"

  for o in $OPTIONS; do
    if [ -x "$o" ]; then
	  echo $o
	  return 0
    fi
  done

  return 1
}

boot_root() {
  # Watches the udev event queue, and exits if all current events are handled
  udevadm settle --timeout=3
  # Kills the current udev running processes, which survived after
  # device node creation events were handled, to avoid unexpected behavior
  killall -9 "${_UDEV_DAEMON##*/}" 2>/dev/null

  # Allow for identification of the real root even after boot
  mkdir -p  /realroot
  ROOT_DISK=$(df | grep "$ROOT_DEVICE" | awk '{print $6}')
  mount -n --move $ROOT_DISK /realroot

  # Unmount the remaining filesystems mounted on /run/media
  for dir in `awk '/\/run\/media\/.* /{print $2}' /proc/mounts`; do
      umount ${dir}
  done
    
  # Move system mounts over to
  # the corresponding directories under the real root filesystem.
  mount -n --move /proc /realroot/proc
  mount -n --move /sys /realroot/sys
  mount -n --move /dev /realroot/dev

  cd /realroot

  # busybox switch_root supports -c option
  exec switch_root -c /dev/console /realroot /sbin/init $CMDLINE ||
      fatal "Couldn't switch_root, dropping to shell"
}
