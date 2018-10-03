# $1=device
resize_partition() {

  if [ -z ${1} ] || [ ! -b ${1} ]; then
    log_warning_msg "block device not present, so skipping resizing partition"
    return 1
  fi

  ln -s /sbin/resize2fs /bin/resize2fs

  local blkdev partnr resizeopts

  blkdev=${1%p*}
  partnr=${1#${blkdev}p}
  resizeopts="resizepart ${partnr} 100"

  [ "$quiet" != "y" ] && log_begin_msg "Resizing $TYPE file system on ${ROOT}"

  if [ -b "${blkdev}" -a ! -z "${partnr}" ]; then
    parted -s ${blkdev} unit % ${resizeopts} print
  else
    log_warning_msg "Block device is not present or partition number is empty"
  fi

  resize2fs -f ${1}
  resize_status="$?"

  [ "$quiet" != "y" ] && log_end_msg
}
