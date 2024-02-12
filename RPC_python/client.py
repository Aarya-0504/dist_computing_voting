import Pyro4

server_uri = "PYRO:example.voting_system@localhost:5193"
voting_system = Pyro4.Proxy(server_uri)

def display_menu():
    print("\n-------------------------------")
    print("Voting Machine")
    print("-------------------------------")
    print("1. Register Voter")
    print("2. Register Party")
    print("3. Cast Vote")
    print("4. Tally Votes")
    print("0. Exit")
    print("-------------------------------")

while True:
    display_menu()
    choice = input("\nEnter your choice: ")
    if choice == "1":
        voter_id = input("Enter Voter ID: ")
        result = voting_system.register_voter(voter_id)
        print(result)
    elif choice == "2":
        party_name = input("Enter Party Name: ")
        result = voting_system.register_party(party_name)
        print(result)
    elif choice == "3":
        voter_id = input("Enter Voter ID: ")
        party_name = input("Enter Party Name: ")
        result = voting_system.vote(voter_id, party_name)
        print(result)
    elif choice == "4":
        tally = voting_system.tally_votes()
        print("Vote Tally:")
        for party, count in tally.items():
            print(f"{party}: {count} votes")
    elif choice == "0":
        print("Exiting...\n")
        break
    else:
        print("Invalid choice! Please try again!")
