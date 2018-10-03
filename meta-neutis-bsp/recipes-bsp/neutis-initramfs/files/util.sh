fatal() {
  echo $1 >$CONSOLE
  echo >$CONSOLE
  exec sh
}

_log_msg() {
  if [ "$quiet" = "y" ]; then return; fi
  printf "$@"
}

log_success_msg() {
  _log_msg "Success: $@\n"
}

log_failure_msg(){
  _log_msg "Failure: $@\n"
}

log_begin_msg() {
	_log_msg "Begin: $@ ... "
}

log_end_msg() {
	_log_msg "done.\n"
}
