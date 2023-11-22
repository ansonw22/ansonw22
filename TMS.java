package hk.edu.polyu.comp.comp2021.tms.model;

import java.util.List;
import java.util.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class TMS {

    public static void main(String[] args) {
        TMSModel taskManagementSystem = new TMSModel();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your command:");
        String inputLine = scanner.nextLine();
        String[] inputTokens = inputLine.split(" ");

        if (inputTokens.length > 0) {
            String command = inputTokens[0];

            if (command.equals("CreatePrimitiveTask")) {
                if (inputTokens.length >= 4) {
                    String taskName = inputTokens[1];
                    String taskDescription = inputTokens[2];
                    double taskDuration = Double.parseDouble(inputTokens[3]);

                    if (inputTokens.length > 4) {
                        String[] prerequisiteTasks = Arrays.copyOfRange(inputTokens, 4, inputTokens.length);
                        //PrimitiveTask task = new PrimitiveTask(taskName, taskDescription, taskDuration,null);
                        //task.setPrerequisites(Arrays.asList(prerequisiteTasks));

                        taskManagementSystem.CreatePrimitiveTask(taskName, taskDescription, taskDuration,null);
                        System.out.println("Primitive task created successfully!");
                    } else {
                        //PrimitiveTask task = new PrimitiveTask(taskName, taskDescription, taskDuration,null);
                        taskManagementSystem.CreatePrimitiveTask(taskName, taskDescription, taskDuration,null);
                        System.out.println("Primitive task created successfully!");
                    }
                } else {
                    System.out.println("Invalid command.");
                }
            } else if (command.equals("CreateCompositeTask")) {
                if (inputTokens.length >= 4) {
                    String taskName = inputTokens[1];
                    String taskDescription = inputTokens[2];
                    double taskDuration = Double.parseDouble(inputTokens[3]);

                    if (inputTokens.length > 4) {
                        String[] subtaskNames = Arrays.copyOfRange(inputTokens, 4, inputTokens.length);
                        CompositeTask task = new CompositeTask(taskName, taskDescription, taskDuration,null);
                        task.setSubtasks(Arrays.asList(subtaskNames));
                        taskManagementSystem.CreateCompositeTask(task);
                        System.out.println("Composite task created successfully!");
                    } else {
                        CompositeTask task = new CompositeTask(taskName, taskDescription, taskDuration,null);
                        taskManagementSystem.CreateCompositeTask(task);
                        System.out.println("Composite task created successfully!");
                    }
                } else {
                    System.out.println("Invalid command.");
                }
            } else if (command.equals("DeleteTask")) {
                if (inputTokens.length >= 2) {
                    String taskName = inputTokens[1];
                    taskManagementSystem.DeleteTask(taskName);
                    System.out.println("Task deleted successfully!");
                } else {
                    System.out.println("Invalid command.");
                }
            } else if (command.equals("ChangeTaskProperty")) {
                if (inputTokens.length >= 4) {
                    String taskName = inputTokens[1];
                    String property = inputTokens[2];
                    String value = inputTokens[3];
                    taskManagementSystem.changeTask(taskName, property, value);
                    System.out.println("Task property changed successfully!");
                } else {
                    System.out.println("Invalid command.");
                }
            } else if (command.equals("PrintTask")) {
                if (inputTokens.length >= 2) {
                    String taskName = inputTokens[1];
                    taskManagementSystem.PrintTask(taskName);
                } else {
                    System.out.println("Invalid command.");
                }
            }
            //*else if (command.equals("PrintAllTasks")) {
            //                taskManagementSystem.printAllTasks();
            //            } else if (command.equals("ReportDuration")) {
            //                double duration = taskManagementSystem.calculateTotalDuration();
            //                System.out.println("Total duration: " + duration);
            //            } else if (command.equals("ReportEarliestFinishTime")) {
            //                String finishTime = taskManagementSystem.getEarliestFinishTime();
            //                System.out.println("Earliest finish time: " + finishTime);
            //            }
            //
            else {
                System.out.println("Invalid command.");
            }
        } else {
            System.out.println("No command specified.");
        }
    }

    public class Task {
        private String name;
        private String description;
        private double duration;
        public Task(String name,String description,double duration){
            this.name=name;
            this.description=description;
            this.duration=duration;
        }
        public String getName(){return name;}
        public String getDescription(){return description;}
        public double getDuration(){return duration;}
        public void setName(String newValue){this.name=newValue;}
        public void setDescription(String newValue){this.description=newValue;}
        public void setDuration(double newValue){this.duration=newValue;}
    }

    public class PrimitiveTask extends Task {
        private  List<Task> prerequisites;
        public PrimitiveTask(String name, String description, double duration, List<Task> prerequisites) {
            super(name,description,duration);
            this.prerequisites = prerequisites;
        }

        public List<Task> getPrerequisites() {return prerequisites;}
        public void setPrerequisites(List<Task> newValue){this.prerequisites=newValue;}
    }

    public class CompositeTask extends Task{
        private List<Task> subtask;
        public CompositeTask(String name, String description, double duration, List<Task> subtask) {
            super(name,description,duration);
            this.subtask = subtask;
        }

        public List<Task> getSubtask() {return subtask;}
        public void setSubtasks(List<Task> newValue){this.subtask=newValue;}
    }

    public class TMSModel {

        private HashMap<String,Task> storage = new HashMap<>();

        public boolean containsName(String inName) {
            return storage.containsKey(inName);
        }
        //Re1:create primitive task//
        public void CreatePrimitiveTask(String name, String description, double duration, List<Task> prerequisites){
            if (containsName(name)) {
                throw new IllegalArgumentException("Repeated task name");
            }
            char firstChar = name.charAt(0);
            if (firstChar == '0' || firstChar == '1' || firstChar == '2' || firstChar == '3' ||
                    firstChar == '4' || firstChar == '5' || firstChar == '6' || firstChar == '7' ||
                    firstChar == '8' || firstChar == '9') {
                System.out.println("Invalid task name. The task name cannot start with a digit.");
                return;
            }
            PrimitiveTask task = new PrimitiveTask( name, description, duration, prerequisites);
            storage.put(name,task);
            try (PrintWriter writer = new PrintWriter( "ToDoList.txt")) {
                writer.print("Name: " + task.getName());
                writer.print("Description: " + task.getDescription());
                writer.print("Duration: " + task.getDuration());
                writer.println("Prerequisites"+task.getPrerequisites());
                writer.flush();
            } catch (FileNotFoundException e) {
                System.out.println("Error saving task to file: " + e.getMessage());
            }


        }

        //Re2: create composite task//
        public void CreateCompositeTask(String name, String description, double duration, List<Task> subset){
            if (containsName(name)) {
                throw new IllegalArgumentException("Repeated task name");
            }
            char firstChar = name.charAt(0);
            if (firstChar == '0' || firstChar == '1' || firstChar == '2' || firstChar == '3' ||
                    firstChar == '4' || firstChar == '5' || firstChar == '6' || firstChar == '7' ||
                    firstChar == '8' || firstChar == '9') {
                System.out.println("Invalid task name. The task name cannot start with a digit.");
                return;
            }
            CompositeTask task= new CompositeTask(name, description, duration, subset);
            storage.put(name,task);
            try (PrintWriter writer = new PrintWriter( "ToDoList.txt")) {
                writer.print("Name: " + task.getName());
                writer.print("Description: " + task.getDescription());
                writer.print("Duration: " + task.getDuration());
                writer.println("Subtask"+task.getSubtask());
                writer.flush();
            } catch (FileNotFoundException e) {
                System.out.println("Error saving task to file: " + e.getMessage());
            }
        }
        //Re3//
        public void DeleteTask(String name){
            if (!containsName(name)) {
                throw new IllegalArgumentException("Task not found");
            }
            storage.remove(name);
            try {
                File file = new File("ToDoList.txt");
                List<String> lines = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("Name: " + name)) {
                        lines.add(line);
                    }
                }
                reader.close();

                FileWriter writer = new FileWriter(file);
                for (String updatedLine : lines) {
                    writer.write(updatedLine + "\n");
                }
                writer.close();

                System.out.println("Deleted task: " + name);
            } catch (IOException e) {
                System.out.println("Error deleting task: " + e.getMessage());
            }
        }
        //Re4//
        public void changeTask(String name, String property, String newValue) {
            if (!containsName(name)) {
                throw new IllegalArgumentException("Task not found");
            }
            // Get the task from storage
            Task task = storage.get(name);
            if (task instanceof PrimitiveTask) {
                PrimitiveTask primitiveTask = (PrimitiveTask) task;
                switch (property.toLowerCase()) {
                    case "name":
                        primitiveTask.setName(newValue);
                        break;
                    case "description":
                        primitiveTask.setDescription(newValue);
                        break;
                    case "duration":
                        try {
                            double duration = Double.parseDouble(newValue);
                            primitiveTask.setDuration(duration);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid duration value.");
                        }
                        break;
                    case "prerequisites":
                        // Assuming newValue is a comma-separated list of task names
                        String[] prerequisiteNames = newValue.split(",");
                        List<Task> prerequisites = new ArrayList<>();
                        for (String prerequisiteName : prerequisiteNames) {
                            Task prerequisite = storage.get(prerequisiteName.trim());
                            if (prerequisite != null) {
                                prerequisites.add(prerequisite);
                            }
                        }
                        primitiveTask.setPrerequisites(prerequisites);
                        break;
                    default:
                        System.out.println("Invalid property.");
                        break;
                }
            } else if (task instanceof CompositeTask) {
                CompositeTask compositeTask = (CompositeTask) task;
                switch (property.toLowerCase()) {
                    case "name":
                        compositeTask.setName(newValue);
                        break;
                    case "description":
                        compositeTask.setDescription(newValue);
                        break;
                    case "subtasks":
                        // Assuming newValue is a comma-separated list of task names
                        String[] subtaskNames = newValue.split(",");
                        List<Task> subtasks = new ArrayList<>();
                        for (String subtaskName : subtaskNames) {
                            Task subtask = storage.get(subtaskName.trim());
                            if (subtask != null) {
                                subtasks.add(subtask);
                            }
                        }
                        compositeTask.setSubtasks(subtasks);
                        break;
                    default:
                        System.out.println("Invalid property.");
                        break;
                }
            }
            // Update the task in the storage
            storage.put(name, task);
        }


        //Re5//
        public void PrintTask(String name){
            if (!containsName(name)) {
                throw new IllegalArgumentException("Task not found");
            }
            Task task = storage.get(name);
            System.out.println(task);
        }

        //req6-10
        public static void printAllTasks() {
            File folder = new File(".");
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".txt")) {
                        System.out.println(file.getName());
                        try {
                            Scanner scanner = new Scanner(file);
                            while (scanner.hasNextLine()) {
                                String line = scanner.nextLine();
                                System.out.println(line);
                            }
                            scanner.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println();
                    }
                }
            }
        }


        public double reportDuration(String name) {
            Task task = tasks.get(name);
            if (task != null) {
                double duration = calculateTaskDuration(task);
                System.out.println("Duration of task '" + name + "': " + duration + " hours");
                return duration;
            } else {
                System.out.println("Task '" + name + "' not found.");
                return -1;
            }
        }

        public double reportEarliestFinishTime(String name) {
            Task task = tasks.get(name);
            if (task != null) {
                double earliestFinishTime = calculateEarliestFinishTime(task);
                System.out.println("Earliest finish time of task '" + name + "': " + earliestFinishTime + " hours");
                return earliestFinishTime;
            } else {
                System.out.println("Task '" + name + "' not found.");
                return -1;
            }
        }

        private double calculateTaskDuration(Task task) {
            if (task.getPrerequisites().isEmpty()) {
                return task.getDuration();
            } else {
                double maxPrerequisiteDuration = 0;
                for (Task prerequisite : task.getPrerequisites()) {
                    double prerequisiteDuration = calculateTaskDuration(prerequisite);
                    if (prerequisiteDuration > maxPrerequisiteDuration) {
                        maxPrerequisiteDuration = prerequisiteDuration;
                    }
                }
                return maxPrerequisiteDuration + task.getDuration();
            }
        }

        private double calculateEarliestFinishTime(Task task) {
            if (task.getPrerequisites().isEmpty()) {
                return task.getDuration();
            } else {
                double maxPrerequisiteFinishTime = 0;
                for (Task prerequisite : task.getPrerequisites()) {
                    double prerequisiteFinishTime = calculateEarliestFinishTime(prerequisite);
                    if (prerequisiteFinishTime > maxPrerequisiteFinishTime) {
                        maxPrerequisiteFinishTime = prerequisiteFinishTime;
                    }
                }
                return maxPrerequisiteFinishTime + task.getDuration();
            }
        }

        class BasicCriterion {
            private String name;
            private String property;
            private String operator;
            private String value;

            public BasicCriterion(String name, String property, String operator, String value) {
                this.name = name;
                this.property = property;
                this.operator = operator;
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public String getProperty() {
                return property;
            }

            public String getOperator() {
                return operator;
            }

            public String getValue() {
                return value;
            }

            public boolean satisfiesCriterion(Task task) {
                switch (getProperty()) {
                    case "name":
                        return task.getName().contains(getValue());

                    case "description":
                        return task.getDescription().contains(getValue());

                    case "duration":
                        double taskDuration = Double.parseDouble(getValue());
                        switch (getOperator()) {
                            case ">":
                                return task.getDuration() > taskDuration;
                            case "<":
                                return task.getDuration() < taskDuration;
                            case ">=":
                                return task.getDuration() >= taskDuration;
                            case "<=":
                                return task.getDuration() <= taskDuration;
                            case "==":
                                return task.getDuration() == taskDuration;
                            case "!=":
                                return task.getDuration() != taskDuration;
                            default:
                                return false;
                        }

                    case "prerequisites":
                        List<String> prerequisiteTasks = Arrays.asList(getValue().split(","));
                        return task.getPrerequisites().containsAll(prerequisiteTasks);

                    case "isPrimitive":
                        return task instanceof PrimitiveTask;

                    default:
                        return false;
                }
            }
        }


        abstract class Task {
            private String name;
            private String description;
            private double duration;
            private List<String> prerequisites;

            public Task(String name, String description, double duration, List<String> prerequisites) {
                this.name = name;
                this.description = description;
                this.duration = duration;
                this.prerequisites = prerequisites;
            }

            public String getName() {
                return name;
            }

            public String getDescription() {
                return description;
            }

            public double getDuration() {
                return duration;
            }

            public List<String> getPrerequisites() {
                return prerequisites;
            }

            public abstract boolean isPrimitive();
        }

        class PrimitiveTask extends Task {
            public PrimitiveTask(String name, String description, double duration, List<String> prerequisites) {
                super(name, description, duration, prerequisites);
            }

            @Override
            public boolean isPrimitive() {
                return true;
            }
        }

        class CompositeTask extends Task {
            public CompositeTask(String name, String description, double duration, List<String> prerequisites) {
                super(name, description, duration, prerequisites);
            }

            @Override
            public boolean isPrimitive() {

                return false;
            }
        }

        //req11-15

        public class TaskSelectionCriteria {
            private Map<String, Predicate<String>> criteria;

            public TaskSelectionCriteria() {
                this.criteria = new HashMap<>();
            }

            public void defineNegatedCriterion(String name1, String name2) {
                Predicate<String> originalCriterion = criteria.get(name2);
                if (originalCriterion != null) {
                    criteria.put(name1, originalCriterion.negate());
                } else {
                    System.out.println("Error: Criterion " + name2 + " not found.");
                }
            }

            public void defineBinaryCriterion(String name1, String name2, String logicOp, String name3) {
                Predicate<String> criterion1 = criteria.get(name2);
                Predicate<String> criterion2 = criteria.get(name3);

                if (criterion1 != null && criterion2 != null) {
                    switch (logicOp) {
                        case "&&":
                            criteria.put(name1, criterion1.and(criterion2));
                            break;
                        case "||":
                            criteria.put(name1, criterion1.or(criterion2));
                            break;
                        default:
                            System.out.println("Error: Invalid logical operator " + logicOp);
                    }
                } else {
                    System.out.println("Error: One or more criteria not found.");
                }
            }

            public void printAllCriteria() {
                System.out.println("All Defined Criteria:");
                for (Map.Entry<String, Predicate<String>> entry : criteria.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }

            public void search(String criterionName) {
                Predicate<String> criterion = criteria.get(criterionName);

                if (criterion != null) {
                    System.out.println("Tasks satisfying criterion '" + criterionName + "':");
                    System.out.println("Sample Task: " + criterion.test("Sample Task"));
                } else {
                    System.out.println("Error: Criterion " + criterionName + " not found.");
                }
            }

            public static void main(String[] args) {
                TaskSelectionCriteria taskSelectionCriteria = new TaskSelectionCriteria();

                taskSelectionCriteria.defineNegatedCriterion("negCriterion1", "criterion1");
                taskSelectionCriteria.defineBinaryCriterion("compositeCriterion1", "criterion2", "&&", "criterion3");

                taskSelectionCriteria.printAllCriteria();

                taskSelectionCriteria.search("compositeCriterion1");
            }
        }

        //Re16//
        public void Quit(){
            System.exit(0);
        }

    }
}
