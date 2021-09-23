# ForkJoin

```java
/**
 * 同步任务状态
 */
@Override
@Transactional
public void updateTaskState() {
    long start = Instant.now().toEpochMilli();
    List<TaskResponseVO> list = taskMapper.listByStateNotEqFinish()
        .stream().filter(task -> !TaskConst.PAUSED.getValue().equals(task.getTaskState().intValue()))
        .collect(Collectors.toList());
    ForkJoinPool pool = new ForkJoinPool();
    TaskState taskState = new TaskState(list);
    List<TaskResponseVO> changedList = pool.invoke(taskState);
    if (!CollectionUtils.isEmpty(changedList)) {
        taskMapper.updateTaskState(changedList);
    }
    pool.shutdown();
    LOG.info("执行修改任务进度耗时: {}秒", (Instant.now().toEpochMilli() - start) / 1000);
}

private static class TaskState extends RecursiveTask<List<TaskResponseVO>> {

    private static RedisUtil redisUtil;
    private static JobConfig config;

    {
        TaskState.redisUtil = ApplicationContextUtil.getBean(RedisUtil.class);
        TaskState.taskMapper = ApplicationContextUtil.getBean(TaskMapper.class);
        TaskState.config = ApplicationContextUtil.getBean(JobConfig.class);
    }

    private List<TaskResponseVO> list;

    public TaskState(List<TaskResponseVO> list) {
        this.list = list;
    }

    @Override
    protected List<TaskResponseVO> compute() {
        if (list.size() <= 10) {
            List<TaskResponseVO> changedList = new ArrayList<>();
            list.forEach(task -> {
                changedList.add(task);
            }
            return changedList;
        } else {
            int mid = (int) Math.ceil(list.size() / 2.0);
            List<List<TaskResponseVO>> partition = Lists.partition(list, mid);
            TaskState leftTask = new TaskState(partition.get(0));
            TaskState rightTask = new TaskState(partition.get(1));
            invokeAll(leftTask, rightTask);
            List<TaskResponseVO> list = new ArrayList<>();
            list.addAll(leftTask.join());
            list.addAll(rightTask.join());
            return list;
        }
    }
}
```

