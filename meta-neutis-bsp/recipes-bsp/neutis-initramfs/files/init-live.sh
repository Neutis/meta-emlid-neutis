#!/bin/sh

early_setup() {
  mkdir -p /proc
  mkdir -p /sys
  mount -t proc proc /proc
  mount -t sysfs sysfs /sys
  mount -t devtmpfs none /dev

  mkdir -p /run
  mkdir -p /var/run

  $_UDEV_DAEMON --daemon

  read_args
  ln -s /proc/self/mounts /etc/mtab

  if [[ -b $DATA_DEVICE ]]; then
    resize_partition $DATA_DEVICE
  fi

  checkfs $DATA_DEVICE data
  checkfs $ROOT_DEVICE root

  udevadm trigger --action=add
}

# Default PATH differs between shells, and is not automatically exported
# by klibc dash. Make it consistent.

PATH=/sbin:/bin:/usr/sbin:/usr/bin

. /scripts/resize.sh
. /scripts/setup.sh
. /scripts/fsck.sh
. /scripts/util.sh


_UDEV_DAEMON=$(udev_daemon)

early_setup

[ -z "$CONSOLE" ] && CONSOLE="/dev/console"

boot_root

echo "..."
echo "Mounted filesystems"
mount | grep media
echo "Available block devices"
cat /proc/partitions
fatal "Cannot find $ROOT_DEVICE or no /sbin/init present , dropping to a shell "

echo "Waiting for root device $ROOT_DEVICE"
