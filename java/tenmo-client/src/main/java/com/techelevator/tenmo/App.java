package com.techelevator.tenmo;

import com.techelevator.tenmo.models.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ConsoleService;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private UserService userService;
    private TransferService transferService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
				new AccountService(API_BASE_URL), new UserService(API_BASE_URL), new TransferService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService,
			   UserService userService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.userService = userService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			try {
				String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
				if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
					viewCurrentBalance();
				} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
					viewTransferHistory();
				} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
					viewPendingRequests();
				} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
					sendBucks();
				} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
					requestBucks();
				} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
					login();
				} else {
					// the only other option on the main menu is to exit
					exitProgram();
				}
			} catch (AccountServiceException | ResourceAccessException e) {
				console.printError(e.getMessage());
				console.next();
			}
		}
	}
	private void viewCurrentBalance() throws AccountServiceException {
		BigDecimal balance = accountService.getBalance(currentUser.getToken());
		console.displayBalance(balance);
		console.next();
	}

	private void viewTransferHistory() {
		boolean finished = false;
		while (!finished) {
			try {
				TransferDetail[] transferList = transferService.viewTransfers(currentUser.getToken());
				if (transferList.length != 0) {
					console.displayTransferList(transferList);
					long transferID = Long.parseLong(console.getUserInput("Please enter transfer ID to view " +
							"details (0 to cancel)"));
					if (transferID == 0) {
						console.output("View request cancelled.");
						finished = true;
					} else {
						TransferDetail transfer = transferService.getTransferDetails(currentUser.getToken(), transferID);
						console.displayTransferDetails(transfer);
						console.next();
						finished = true;
					}
				} else {
					console.output("No transfers to display.");
					finished = true;
				}
			} catch (NumberFormatException e) {
				console.output("Please enter the transfer ID number or 0 to cancel.");
			}
		}
		
	}


	private void viewPendingRequests() {
		boolean finished = false;
		while (!finished) {
			try {
				TransferRequest[] pendingRequests = transferService.viewPendingRequests(currentUser.getToken());
				if (pendingRequests.length != 0) {
					console.displayPendingRequests(pendingRequests);
					long transferID = Long.parseLong(console.getUserInput("Please enter transfer ID to approve/reject " +
							"(0 to cancel)"));
					if (transferID == 0) {
						console.output("Approve/reject pending requests cancelled.");
						finished = true;
					} else {
						console.displayPendingRequestOptions();
						long option = Long.parseLong(console.getUserInput("Please choose an option"));
						TransferRequest requestToExecute = selectTransfer(pendingRequests, transferID);
						TransferStatus status = transferService.executeTransfer(currentUser.getToken(), option, requestToExecute);
						console.output("Transfer Status: " + status.getTransferStatusDesc());
						console.next();
						finished = true;
					}
				} else {
					console.output("No currently pending requests.");
					finished = true;
				}
			} catch (NumberFormatException e) {
				console.output("Please enter the transfer ID number or 0 to cancel.");
			} //catch an error for not entering the right option
		}
	}

	private TransferRequest selectTransfer(TransferRequest[] pendingRequests, long transferId) {
    	TransferRequest transfer = new TransferRequest();
    	for(TransferRequest request : pendingRequests) {
    		if (request.getTransferId() == transferId) {
    			transfer = request;
    			break;
			}
		}
    	return transfer;
	}

	private void sendBucks() {
    	boolean finished = false;
    	while (!finished) {
			try {
				User[] userList = userService.listUsers(currentUser.getToken());
				console.displayUserList(userList);

				long toUserID = Long.parseLong(console.getUserInput("Enter ID of user you are sending to (0 to cancel)"));

				if (toUserID == 0) {
					console.output("Transaction cancelled.");
					finished = true;
				} else {
					BigDecimal transferAmount = new BigDecimal(console.getUserInput("Enter amount"));

					TransferStatus transferStatus = transferService.sendBucks(currentUser.getToken(), toUserID, transferAmount);
					console.output("Transfer Status: " + transferStatus.getTransferStatusDesc());
					finished = true;
				}

			} catch (NumberFormatException e) {
				console.output("Please provided necessary information and only enter numeric values.");
			}
		}
	}

	private void requestBucks() {
		boolean finished = false;
		while (!finished) {
			User[] userList = userService.listUsers(currentUser.getToken());
			console.displayUserList(userList);

			long fromUserId = Long.parseLong(console.getUserInput("Enter ID of user you are requesting from (0 to cancel)"));

			if (fromUserId == 0) {
				console.output("Request cancelled.");
				finished = true;
			} else {
				BigDecimal transferAmount = new BigDecimal(console.getUserInput("Enter amount"));
				TransferStatus status = transferService.requestTransfer(currentUser.getToken(), fromUserId, transferAmount);
				console.output("Requested Transfer Status: " + status.getTransferStatusDesc());
				finished = true;
			}
		}
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
