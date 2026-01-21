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
        ArrayList<Task> past_tasks = new ArrayList<>();
        Task new_task;
        while(true){
            String task = inp.nextLine();
            String[] task_split = task.split(" ",2);
            String command = task_split[0];
            String[] task_details;
            System.out.println(command);
            switch(command){
            case "bye":
                return;
            case "list":
                list(past_tasks);
                break;
            case "mark", "unmark":
                Task current_task = past_tasks.get(Integer.parseInt(task.split(" ")[1])-1);
                current_task.setDone(command.equals("mark"));
                mark(current_task);
                break;
            case "todo":
                new_task = new Todo(task_split[1]);
                past_tasks.add(new_task);
                System.out.println("Noted with thanks, \nadded " + new_task + " to ur list\nCurrently u have " + past_tasks.size() + " of stuff");
                break;
            case "deadline":
                task_details = task_split[1].split(" /by ");
                new_task = new Deadline(task_details[0],task_details[1]);
                past_tasks.add(new_task);
                System.out.println("Noted with thanks, \nadded " + new_task + " to ur list\nCurrently u have " + past_tasks.size() + " of stuff");
                break;
            case "event":
                task_details = task_split[1].split(" /from | /to ");
                new_task = new Event(task_details[0],task_details[1],task_details[2]);
                past_tasks.add(new_task);
                System.out.println("Noted with thanks, \nadded " + new_task + " to ur list\nCurrently u have " + past_tasks.size() + " of stuff");
                break;
            default:
                System.out.println("U entered something wong");
            }}
    }

    public static void list(ArrayList<Task> past_tasks){
        int i =1;
        for(Task task: past_tasks)
        {
            System.out.println(i + ". " + task);
            i++;
        }
        System.out.println("\n");
    }

    public static void mark(Task task){
        if(task.getDone()){
            System.out.println("ok slay its done\n" + task);
        }else{
            System.out.println("not marked\n" + task);
        }
        System.out.println();
    }
}
