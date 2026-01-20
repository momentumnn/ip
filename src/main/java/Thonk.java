import java.util.ArrayList;
import java.util.Scanner;

public class Thonk {
    public static void main(String[] args) {
        banner();
        echo();

        System.out.println("ok bye bye\n" );

    }

    public static void banner(){
        String logo ="""
               _____ _                 _
              |_   _| |__   ___  _ __ | | __
                | | | '_ \\ / _ \\| '_ \\| |/ /
                | | | | | | (_) | | | |   <
                |_| |_| |_|\\___/|_| |_|_|\\_\\
              """;
        String divider = "_______________________________\n\n";
        System.out.println("Hello from\n" + logo + "what u want \n" + divider);
    }
    public static void echo(){
        String divider = "_______________________________\n\n";
        Scanner inp = new Scanner(System.in);
        ArrayList<Task> past_tasks = new ArrayList<>();
        int task_complete = 0;
        while(true){
            String task = inp.nextLine();
            switch(task.split(" ")[0]){
                case "bye":
                    return;
                case "list":
                    list(past_tasks);
                    break;
                case "mark":
                    task_complete =  Integer.parseInt(task.split(" ")[1]) - 1;
                    if(task.split(" ").length >= 2){
                        mark(task_complete, past_tasks, true);
                    }
                    break;
                case "unmark":
                    task_complete =  Integer.parseInt(task.split(" ")[1]) - 1;
                    if(task.split(" ").length >= 2){
                        mark(task_complete, past_tasks, false);
                    }
                    break;

                default:
                    Task new_task = new Task(task);
                    past_tasks.add(new_task);
                    String output= past_tasks.toString();
//                            .replace("[","")
//                            .replace("]","");
                    System.out.println("i also can " + output + "\n" + divider);

            }}
    }

    public static void list(ArrayList<Task> past_tasks){
        int i =1;
        for(Task task: past_tasks)
        {
            System.out.println(i + ". " + task.toString());
            i++;
        };
        System.out.println("\n");
    }

    public static void mark(int task_complete, ArrayList<Task> past_tasks, boolean marked){
        Task current = past_tasks.get(task_complete);
        current.setDone(marked);
        if(marked){
            System.out.println("ok slay its done\n" + current.toString());
        }else{
            System.out.println("not marked\n" + current.toString());
        }
        System.out.println();
    }
}
