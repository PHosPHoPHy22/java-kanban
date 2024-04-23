import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefaults();
        Task task = taskManager.create(new Task("new Task", Status.NEW, "Description"));
        System.out.println("Create task: " + task);

        Task taskFromManager = taskManager.get(task.getId());
        System.out.println("Get task: " + taskFromManager);

        taskFromManager.setName("name");
        taskManager.update(taskFromManager);
        System.out.println("Update task: " + taskFromManager);

        taskManager.delete(taskFromManager.getId());
        System.out.println("Delete: " + task);

        Task task1 = taskManager.create(new Task("new Task", Status.NEW, "Description"));
        System.out.println("Create task: " + task1);


        Epic epic = new Epic("test epic", "test");
        Epic createdEpic = taskManager.createEpic(epic);
        System.out.println("Create epic: " + createdEpic);

        SubTask subTask1 = new SubTask("test subTask1", Status.NEW, "test", createdEpic);
        taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("test subTask2", Status.NEW, "test", createdEpic);
        taskManager.createSubTask(subTask2);


        System.out.println("Created epic: " + createdEpic);

        subTask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask1);

        System.out.println("Status After IN_PROGRESS: " + createdEpic);

        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);
        subTask2.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask2);

        System.out.println("Status After DONE: " + createdEpic);

        SubTask subTask3 = taskManager.createSubTask(new SubTask("test subTask3", Status.NEW, "test", createdEpic));

        System.out.println("Status After new Subtask: " + createdEpic);

        taskManager.deleteSubTask(subTask3.getId());

        System.out.println("Status After Delete: " + createdEpic);

        var subTasks = taskManager.getSubTasks();
        System.out.println(subTasks);
        System.out.println(subTasks.size());

        taskManager.deleteEpic(createdEpic.getId());
        subTasks = taskManager.getSubTasks();
        System.out.println(subTasks);
        System.out.println(subTasks.size());

        Task task2 = taskManager.create(new Task("New Task", Status.NEW, "new Task"));
        System.out.println("Tasks: " + taskManager.getAll());
        taskManager.deleteTasks();
        System.out.println("Tasks: " + taskManager.getAll());

        var newEpic = taskManager.createEpic(new Epic("newTestEpic", "Epic"));
        SubTask subTask5 = new SubTask("SubTask5", Status.NEW, "SubTask5 test", newEpic);
        SubTask subTask6 = new SubTask("SubTask6", Status.NEW, "SubTask6 test", newEpic);
        SubTask subTask7 = new SubTask("SubTask7", Status.NEW, "SubTask7 test", newEpic);
        SubTask subTask8 = new SubTask("SubTask8", Status.NEW, "SubTask8 test", newEpic);
        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);
        taskManager.createSubTask(subTask7);
        taskManager.createSubTask(subTask8);

        System.out.println("newEpic: " + newEpic);

        taskManager.deleteSubtasks();

        System.out.println("newEpic delete: " + newEpic);

        System.out.println(taskManager.getEpics());

        taskManager.deleteEpics();

    }
}
//