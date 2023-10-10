$line = jps|Select-String -Pattern "SimpleDeadLock"
#Write-Output ($line)
$pid1 = ($line -split ' ')[0]
#Write-Output ($pid1)
$is_failed = jstack $pid1|Select-String -Pattern "deadlock" -Quiet -CaseSensitive

if($is_failed) {
   Write-Output ('failed')
}else {
   Write-Output ('passed')
}

#taskkill /pid $pid1 /f

Clear-Variable -Name line
Clear-Variable -Name pid1
Clear-Variable -Name is_failed

