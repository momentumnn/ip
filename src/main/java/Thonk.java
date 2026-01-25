import java.util.ArrayList;
import java.util.Scanner;

public class Thonk {
    public static String logo ="""
               _____ _                 _
              |_   _| |__   ___  _ __ | | __
                | | | '_ \\ / _ \\| '_ \\| |/ /
                | | | | | | (_) | | | |   <
                |_| |_| |_|\\___/|_| |_|_|\\_\\
              """;
    public static String divider = "_______________________________\n\n";

    public static ArrayList<Task> pastTasks = new ArrayList<>();

    public static void main(String[] args) {
        banner();
        echo();

        System.out.println("ok bye bye\n" );
    }

    public static void banner(){
        System.out.println("Hello from\n" + logo + "what u want \n" + divider);
    }
    public static void echo(){
        Scanner input = new Scanner(System.in);
        Task newTask;
        while(true) {
            try {
                // full command
                String task = input.nextLine();
                // stripped command, left with everything after command.
                // e.g "deadline buy bread" into "buy bread"
                String[] taskSplit = task.split(" ", 2);
                Command command = Command.valueOf(taskSplit[0].toUpperCase());
                String[] taskDetails;
                switch (command) {
                case BYE:
                    return;
                case LIST:
                    list();
                    break;
                case MARK, UNMARK:
                    mark(task);
                    break;
                case TODO:
                    if (taskSplit.length == 1) {
                        throw new ThonkException("todo");
                    }
                    newTask = new Todo(taskSplit[1]);
                    add(newTask);
                    break;
                case DEADLINE:
                    taskDetails = taskSplit[1].split(" /by ");
                    if (taskDetails.length == 1) {
                        throw new ThonkException("deadline");
                    }
                    newTask = new Deadline(taskDetails[0], taskDetails[1]);
                    add(newTask);
                    break;
                case EVENT:
                    taskDetails = taskSplit[1].split(" /from | /to ");
                    if (taskDetails.length != 3) {
                        throw new IncompleteCommand(taskSplit[1]);
                    }
                    newTask = new Event(taskDetails[0], taskDetails[1], taskDetails[2]);
                    add(newTask);
                    break;
                case DELETE:
                    delete(task);
                    break;
                default:
                    throw new ThonkException("U entered something wong");
                }
            }catch (IllegalArgumentException e){
                System.out.println("Invalid command");
            }catch (ThonkException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void list(){
        if(pastTasks.isEmpty()){
            System.out.println("There is nothing to do.");
            return;
        }
        int i =1;
        for(Task task: pastTasks)
        {
            System.out.println(i + ". " + task);
            i++;
        }
        System.out.println("\n");
    }

    public static void add(Task task){
        pastTasks.add(task);
        System.out.println("Noted with thanks, \nadded " + task + " to ur list\nCurrently u have " + pastTasks.size() + " of stuff");
    }

    public static void delete(String task){
        Task currentTask = pastTasks.get(Integer.parseInt(task.split(" ")[1]) - 1);
        pastTasks.remove(currentTask);
        System.out.println("Noted with thanks, \nsay bye bye to  " + currentTask + " from ur list\nCurrently u have " + pastTasks.size() + " of stuff");
    }

    public static void mark(String task){
        int max = pastTasks.size();
        String regex = "[1-" + max + "]";
        String[] taskSplit = task.split(" ");
        if(taskSplit.length==1 ){
            throw new ThonkException("Include task number la wlao");
        }
        if(!taskSplit[1].matches(regex)){
            throw new ThonkException("out of bounds");
        }
        Task currentTask = pastTasks.get(Integer.parseInt(task.split(" ")[1]) - 1);
        currentTask.setDone(taskSplit[0].equals("mark"));
        if(currentTask.getDone()){
            System.out.println("ok slay its done\n" + currentTask);
        }else{
            System.out.println("not marked\n" + currentTask);
        }
        System.out.println();
    }
}
