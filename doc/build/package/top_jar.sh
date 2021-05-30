echo "查询所有jar包运行内存"
pids=`jps -l | awk '{printf $1","}'`
top -M -p $pids