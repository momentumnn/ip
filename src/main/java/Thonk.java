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

    public static ArrayList<Task> past_tasks = new ArrayList<>();

    public static void main(String[] args) {
        banner();
        echo();

        System.out.println("ok bye bye\n" );
    }

    public static void banner(){
        System.out.println("Hello from\n" + logo + "what u want \n" + divider);
    }
    public static void echo(){
        Scanner inp = new Scanner(System.in);
        Task new_task;
        while(true) {
            try {
                // full command
                String task = inp.nextLine();
                // stripped command, left with everything after command.
                // e.g "deadline buy bread" into "buy bread"
                String[] task_split = task.split(" ", 2);
                String command = task_split[0];
                String[] task_details;
                switch (command) {
                case "bye":
                    return;
                case "list":
                    list(past_tasks);
                    break;
                case "mark", "unmark":
                    mark(task);
                    break;
                case "todo":
                    if (task_split.length == 1) {
                        throw new ThonkException("todo");
                    }
                    new_task = new Todo(task_split[1]);
                    add(new_task);
                    break;
                case "deadline":
                    task_details = task_split[1].split(" /by ");
                    if (task_details.length == 1) {
                        throw new ThonkException("deadline");
                    }
                    new_task = new Deadline(task_details[0], task_details[1]);
                    add(new_task);
                    break;
                case "event":
                    task_details = task_split[1].split(" /from | /to ");
                    if (task_details.length != 3) {
                        throw new IncompleteCommand(task_split[1]);
                    }
                    new_task = new Event(task_details[0], task_details[1], task_details[2]);
                    add(new_task);
                    break;
                case "delete":
                    delete(task);
                    break;
                default:
                    throw new ThonkException("U entered something wong");
                }
            }catch (IllegalArgumentException e){
                System.out.println("Invalid command");
            }catch (ThonkException e){
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void list(){
        if(past_tasks.isEmpty()){
            System.out.println("There is nothing to do.");
            return;
        }
        int i =1;
        for(Task task: past_tasks)
        {
            System.out.println(i + ". " + task);
            i++;
        }
        System.out.println("\n");
    }

    public static void add(Task task){
        past_tasks.add(task);
        System.out.println("Noted with thanks, \nadded " + task + " to ur list\nCurrently u have " + past_tasks.size() + " of stuff");
    }

    public static void delete(String task){
        Task current_task = past_tasks.get(Integer.parseInt(task.split(" ")[1]) - 1);
        past_tasks.remove(current_task);
        System.out.println("Noted with thanks, \nsay bye bye to  " + current_task + " from ur list\nCurrently u have " + past_tasks.size() + " of stuff");
    }

    public static void mark(String task){
        int max = past_tasks.size();
        String regex = "[1-" + max + "]";
        String[] task_split = task.split(" ");
        if(task_split.length==1 ){
            throw new ThonkException("Include task number la wlao");
        }
        if(!task_split[1].matches(regex)){
            throw new ThonkException("out of bounds");
        }
        Task current_task = past_tasks.get(Integer.parseInt(task.split(" ")[1]) - 1);
        current_task.setDone(task_split[0].equals("mark"));
        if(current_task.getDone()){
            System.out.println("ok slay its done\n" + current_task);
        }else{
            System.out.println("not marked\n" + current_task);
        }
        System.out.println();
    }
}
