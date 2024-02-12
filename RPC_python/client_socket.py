import socket
import pickle

def send_request(action, data):
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(('localhost', 9999))
    request = {"action": action, **data}
    client_socket.send(pickle.dumps(request))
    response = client_socket.recv(1024)
    print(pickle.loads(response))
    client_socket.close()

def main():
    while True:
        print("\n-------------------------------")
        print("Voting Machine")
        print("-------------------------------")
        print("1. Register Voter")
        print("2. Register Party")
        print("3. Cast Vote")
        print("4. Tally Votes")
        print("0. Exit")
        print("-------------------------------")

        choice = input("\nEnter your choice: ")
        if choice == "1":
            voter_id = input("Enter Voter ID: ")
            send_request("register_voter", {"voter_id": voter_id})
        elif choice == "2":
            party_name = input("Enter Party Name: ")
            send_request("register_party", {"party_name": party_name})
        elif choice == "3":
            voter_id = input("Enter Voter ID: ")
            party_name = input("Enter Party Name: ")
            send_request("vote", {"voter_id": voter_id, "party_name": party_name})
        elif choice == "4":
            send_request("tally_votes", {})
        elif choice == "0":
            print("Exiting...\n")
            break
        else:
            print("Invalid choice! Please try again!")

if __name__ == "__main__":
    main()
