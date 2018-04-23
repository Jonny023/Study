package com.china.cq.sun.base.activiti;


import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * Created by Administrator on 2017/8/7.
 */
public class ActivitiManage {
    private static final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    private static final RepositoryService repositoryService = processEngine.getRepositoryService();
    private static final RuntimeService runtimeService = processEngine.getRuntimeService();
    private static final TaskService taskService = processEngine.getTaskService();
    private static final HistoryService historyService = processEngine.getHistoryService();
    /*
    发布请假流程  *.zip包放入conf下的activi目录下                                                        流程名称            流程Zip包包名
    Deployment deployment= ActivitiManage.deploymentProcessDefinition_zip("请假流程实例","activiTest.zip");

    启动流程添加操作人员
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("employee", "user_employee");//多个人员用","隔开
    variables.put("boos", "user_boos");
    ProcessInstance processInstance= ActivitiManage.startProcess(deployment,variables);

    查询当前用户任务列表
    def userTaskList=ActivitiManage.taskByAssignee(user);

        单个任务处理（任务已执行操作）
        ActivitiManage.completeTask(userTask[0].id);

        返回上一个节点
        ActivitiManage.backProcess(userTask[0].id);

    判断流程是否结束
    ActivitiManage.processIsEnd(processInstance.getProcessInstanceId())

        删除流程
        ActivitiManage.deleteProcess(deployment.getId())

    查询所有需要执行的任务
    ActivitiManage.queryAllTask()
    */



    /**
     *流程发布
     * 部署流程定义
     * @param activitiName 流程实例名字
     * @param activitiZip   文件的路径zip包名
     * @return   Deployment 流程
     */
    public static Deployment deploymentProcessDefinition_zip(String activitiName, String activitiZip){
        Deployment deployment = null;
        InputStream inputStream=null;
        try{
            inputStream =ActivitiManage.class.getClassLoader().getResourceAsStream("\\activi\\"+activitiZip );
            if(inputStream!=null){
                ZipInputStream zipInputStream = new ZipInputStream(inputStream);
                deployment=repositoryService //
                        .createDeployment().name(activitiName)
                        .addZipInputStream(zipInputStream)
                        .deploy();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return deployment;
    }

    /**
     * 添加任务处理人
     * @param deployment 发布对象
     * @param variables Map<String, Object> variables = new HashMap<String, Object>(); variables.put("employee", "user_employee");
     * @return
     */
    public static ProcessInstance startProcess(Deployment deployment, Map<String, Object> variables) {
        ProcessInstance processInstance = null;
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).list();
        if (!processDefinitionList.isEmpty()) {
            String key =  processDefinitionList.get(0).getKey();
            processInstance = runtimeService.startProcessInstanceByKey(key, variables);
        }
        return processInstance;
    }



    /**
     * 根据执行人查询任务
     *
     * @param assignee：执行人
     * @return List<Task>
     */
    public static List<Task> taskByAssignee(String assignee) {

        List<Task> taskList = taskService.createTaskQuery().orderByExecutionId().desc()
                .taskAssignee(assignee).list();
        return taskList;
    }

    /**
     * 查询所有任务
     *
     * @return List<Task>
     */
    public static List<Task> queryAllTask() {
        List<Task> taskList = taskService.createTaskQuery().list();
        return taskList;
    }

    /**
     * 执行任务
     *
     * @param taskId：任务ID
     */
    public static void completeTask(String taskId) {
        taskService.complete(taskId);
    }
    /**
     * 执行任务并分配某一节点操作用户
     *
     * @param taskId：任务ID
     */
    public static void completeTask(String taskId,Map<String, Object> varMap) {
        taskService.complete(taskId,varMap);
    }

    /**
     * 删除流程
     *
     * @param deploymentId：流程发布ID
     */
    public static void deleteProcess(String deploymentId) {
        //级联删除（不管该流程是否有正在执行的任务，一律删除掉全部信息）
        repositoryService.deleteDeployment(deploymentId, true);
    }





    /**
     * 流程回退上一级方法
     */
    public static void backProcess(String taskId) {
        try {

            Map<String, Object> variables = null; //用于存放流程执行角色
            HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult(); // 取得当前任务.当前任务节点

            ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(currTask.getProcessInstanceId()).singleResult();// 取得流程实例，流程实例
            variables = instance.getProcessVariables();

            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(currTask.getProcessDefinitionId()); // 取得流程定义

            ActivityImpl currActivity = ((ProcessDefinitionImpl) definition).findActivity(currTask.getTaskDefinitionKey()); // 取得上一步活动

            //连线和当前活动出口
            List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
            List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
            List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
            for (PvmTransition pvmTransition : pvmTransitionList) {
                oriPvmTransitionList.add(pvmTransition);
            }
            pvmTransitionList.clear();

            // 建立新出口
            List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
            for (PvmTransition nextTransition : nextTransitionList) {
                PvmActivity nextActivity = nextTransition.getSource();
                ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition)
                        .findActivity(nextActivity.getId());
                TransitionImpl newTransition = currActivity
                        .createOutgoingTransition();
                newTransition.setDestination(nextActivityImpl);
                newTransitions.add(newTransition);
            }

            // 完成任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(instance.getId())
                    .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                taskService.complete(task.getId(), variables);
                historyService.deleteHistoricTaskInstance(task.getId());
            }

            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                currActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {
                pvmTransitionList.add(pvmTransition);
            }

            ////////// 完成 ///////////
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    图输出流
     def processInstanceId;
     if(processInstanceId){
        InputStream ins;
        OutputStream output;
        try {
           ins = ActivitiManage.viewImg(processInstanceId);
           output=response.getOutputStream();
           if(ins != null) {
               byte[] b = new byte[1024];
               int len;
               while ((len = ins.read(b, 0, 1024))>0) {
                   output.write(b, 0, len);
               }
            }
            ins.close();
            output.close();
        }catch (Exception e){
           ins.close();
           output.close();
        }
     }
     *
     */

    /**
     * 流程跟踪，标注当前活动流程图
     */
    public static InputStream viewImg(String processInstanceId) {
        InputStream imageStream = null;
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();

        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();

        ProcessDefinitionEntity definitionEntity =
                (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        List<HistoricActivityInstance> highLightedActivitList =
                historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();

        List<String> highLightedActivitis = new ArrayList<>();

        List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitList);

        for (HistoricActivityInstance tempActivity : highLightedActivitList) {
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }

        imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows, "宋体", "宋体", null, 0.5);
        return imageStream;
    }

    /**
     * 获取需要高亮的线
     *
     * @param processDefinitionEntity
     * @param historicActivityInstances
     * @return
     */
    private static List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
                                                    List<HistoricActivityInstance> historicActivityInstances) {
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
            ActivityImpl activityImpl = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息

            List<ActivityImpl> sameStartTimeNodes = new ArrayList<>();// 用以保存后需开始时间相同的节点
            ActivityImpl sameActivityImpl1 = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i + 1).getActivityId());

            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
                    // 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    // 有不相同跳出循环
                    break;
                }
            }

            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();

                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }

    /***
     * 判断流程是否结束
     * @param processInstanceId 流程实例对象Id processInstance.getProcessInstanceId()
     * @return true(结束)/false（进行中）
     */
    public static Boolean processIsEnd(String processInstanceId){
        ProcessInstance rpi = runtimeService//
                .createProcessInstanceQuery()//创建流程实例查询对象
                .processInstanceId(processInstanceId)
                .singleResult();
        if(rpi==null){
            return true;
        }
        return false;
    }

    /***
     * 返回任务指定流程正在进行的单个任务
     * @param processInstanceId
     * @return
     */
    public static Task searchTask(String processInstanceId){
        Task task =taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        return task;
    }

    /**
     * 查询组任务
     *
     * @param candidateUser：组任务参与者
     */
    public static List<Task> queryGroupTaskList(String candidateUser) {
        List<Task> taskList = taskService.createTaskQuery().orderByTaskCreateTime().desc().taskCandidateUser(candidateUser).list();
        List<Task> rsList = new ArrayList<>();
        for (Task t : taskList) {
            if (t.isSuspended()) {
                continue;
            }
            rsList.add(t);
        }
        return rsList;
    }

    /**
     * 得到该节点 的未做的任务
     *
     * @param candidateUser   组任务参与者
     * @param taskDefKey：节点名称
     * @return 该节点的组任务
     */
    public static List<Task> queryTaskNode(String candidateUser, String taskDefKey) {
        List<Task> taskList = queryGroupTaskList(candidateUser);
        List<Task> tasks = new ArrayList<Task>();
        for (Task t : taskList) {
            if (t.getTaskDefinitionKey().equals(taskDefKey)) {
                tasks.add(t);
            }
        }
        return tasks;
    }

}
