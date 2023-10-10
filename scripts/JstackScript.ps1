$line = jps|Select-String -Pattern "SimpleDeadLock"
$pid1 = ($line -split ' ')[0]
$is_failed = jstack $pid1|Select-String -Pattern "deadlock" -Quiet

if($is_failed) {
   Write-Output ('failed')
}else {
   Write-Output ('passed')
}