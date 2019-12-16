package challenge;

import challenge.repository.InMemoryHistoryStore;
import challenge.repository.Store;
import challenge.service.DataStoreService;
import challenge.service.DataStoreServiceImpl;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Store store = new InMemoryHistoryStore();
        DataStoreService dataStoreService = new DataStoreServiceImpl(store);
        Scanner scanner = new Scanner(System.in);
        boolean quitted = false;

        System.out.println("Please enter a command or QUIT to terminate:");

        // terminates the loop if "QUIT" was a command
        while (!quitted) {

            if (scanner.hasNext()) {

                String[] userInput = scanner.nextLine().split("\\s+"); // remove whitespaces
                try{

                    switch (userInput[0].toUpperCase()) {

                        case "CREATE":
                            System.out.println(dataStoreService.
                                    create(Integer.parseInt(userInput[1]), Integer.parseInt(userInput[2]), userInput[3]));
                            break;
                        case "UPDATE":
                            System.out.println(dataStoreService.
                                    update(Integer.parseInt(userInput[1]), Integer.parseInt(userInput[2]), userInput[3]));
                            break;
                        case "DELETE":
                            Integer timestamp = null;
                            if (userInput.length == 3)
                                timestamp = Integer.parseInt(userInput[2]);
                            System.out.println(dataStoreService.
                                    delete(Integer.parseInt(userInput[1]), timestamp));
                            break;

                        case "GET":
                            System.out.println(dataStoreService.
                                    get(Integer.parseInt(userInput[1]), Integer.parseInt(userInput[2])));
                            break;
                        case "LATEST":
                            System.out.println(dataStoreService.
                                    latest(Integer.parseInt(userInput[1])));
                            break;
                        case "QUIT":
                            quitted = true;
                            break;
                        default:
                            System.out.println("Please enter a valid command:");
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());;
                }
            }
        }
    }
}
