package com.techelevator.view;


import com.techelevator.tenmo.models.TransferDetail;
import com.techelevator.tenmo.models.TransferRequest;
import com.techelevator.tenmo.models.User;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public void output(String output) {
		out.println();
		out.println(output);
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public void next() {
		System.out.println("\nPress Enter to continue...");
		in.nextLine();
	}

	public void printError(String errorMessage) {
		System.err.println(errorMessage);
	}

	public void displayBalance(BigDecimal balance) {
		System.out.println("\nYour current account balance is: $" + balance);
	}

	public void displayUserList(User[] userList) {
		System.out.println("---------------------------------------------");
		System.out.println("Users");
		System.out.println("ID 					Name");
		System.out.println("---------------------------------------------");
		for (User user : userList) {
			System.out.println(user.getId() + " 				" + user.getUsername());
		}
		System.out.println("-----------------");
	}

	public void displayTransferList(TransferDetail[] transferList) {
		System.out.println("-------------------------------------------------------");
		System.out.println("Transfers");
		System.out.println("ID 				From/To						Amount");
		System.out.println("-------------------------------------------------------");
		for (TransferDetail transfer : transferList) {
			System.out.println(transfer.getTransferId() + " 			" + transfer.getDisplayFromOrTo()
			+ "					$" + transfer.getAmount());
		}
		System.out.println("-------------------");
	}

	public void displayTransferDetails(TransferDetail transfer) {
		System.out.println();
		System.out.println("-------------------------------------------------------");
		System.out.println("Transfer Details");
		System.out.println("-------------------------------------------------------");
		System.out.println("Id: " + transfer.getTransferId());
		System.out.println("From: " + transfer.getFromUsername());
		System.out.println("To: " + transfer.getToUsername());
		System.out.println("Type: " + transfer.getTransferTypeDesc());
		System.out.println("Status: " + transfer.getTransferStatusDesc());
		System.out.println("Amount: $" + transfer.getAmount());

	}

	public void displayPendingRequests(TransferRequest[] pendingRequests) {
		System.out.println();
		System.out.println("-------------------------------------------------------");
		System.out.println("Pending Transfers");
		System.out.println("ID 			To 						Amount");
		System.out.println("-------------------------------------------------------");
		for (TransferRequest request : pendingRequests) {
			System.out.println(request.getTransferId() + " 		" + request.getToUsername() +
					" 					" +	request.getTransferAmount());
		}
		System.out.println("-------------------");
	}

	public void displayPendingRequestOptions() {
		System.out.println();
		System.out.println("1: Approve");
		System.out.println("2: Reject");
		System.out.println("0: Don't approve or reject");
		System.out.println("-------------------");
	}
}






