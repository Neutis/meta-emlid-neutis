ln -s /sbin/resize2fs /bin/resize2fs

# $1=device
resize_partition() {

  if [ -z ${1} ] || [ ! -b ${1} ]; then
    log_warning_msg "block device not present, so skipping resizing partition"
    return 1
  fi

  local blkdev partnr resizeopts currpartsize

  blkdev=${1%p*}
  partnr=${1#${blkdev}p}

  currpartsize=$(parted -s ${blkdev} unit % print 2>/dev/null | grep '^[ \n]'${partnr} | awk '{print $3}' | grep -o '^[^.A-Z%]*')

  if [ ! ${currpartsize} ]; then
    log_warning_msg "unable to get size of ${1}"
    return 1
  fi

  if [ ${currpartsize} -ge 100 ]; then
    return 0
  elif [ ${currpartsize} -lt 100 ];then
    true
  else
    log_warning_msg "unable to get valid size of ${1}"
    return 1
  fi

  resizeopts="resizepart ${partnr} 100"

  [ "$quiet" != "y" ] && log_begin_msg "Resizing $TYPE file system on ${ROOT}"

  if [ -b "${blkdev}" -a ! -z "${partnr}" ]; then
    parted -s ${blkdev} unit % ${resizeopts} print
  else
    log_warning_msg "block device is not present or partition number is empty"
  fi

  resize2fs -f ${1}
  resize_status="$?"

  [ "$quiet" != "y" ] && log_end_msg
}

