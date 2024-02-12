import socket
import pickle

class VotingSystem:
    def __init__(self):
        self.voters = set()
        self.parties = {}

    def register_voter(self, voter_id):
        self.voters.add(voter_id)
        return f"Voter {voter_id} registered successfully"

    def register_party(self, party_name):
        self.parties[party_name] = 0
        return f"Party {party_name} registered successfully"

    def vote(self, voter_id, party_name):
        if voter_id not in self.voters:
            return "Voter not registered"
        if party_name not in self.parties:
            return "Party not registered"
        self.parties[party_name] += 1
        return f"Vote cast for {party_name} successfully"

    def tally_votes(self):
        return self.parties

def handle_client_connection(conn, voting_system):
    while True:
        data = conn.recv(1024)
        if not data:
            break

        request = pickle.loads(data)
        if request["action"] == "register_voter":
            result = voting_system.register_voter(request["voter_id"])
        elif request["action"] == "register_party":
            result = voting_system.register_party(request["party_name"])
        elif request["action"] == "vote":
            result = voting_system.vote(request["voter_id"], request["party_name"])
        elif request["action"] == "tally_votes":
            result = voting_system.tally_votes()
        else:
            result = "Invalid action"

        conn.send(pickle.dumps(result))

    conn.close()

def main():
    voting_system = VotingSystem()
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(('localhost', 9999))
    server_socket.listen(5)

    print("Server is listening...")
    #print(f"Connected to {addr}")
    while True:
        conn, addr = server_socket.accept()
        
        handle_client_connection(conn, voting_system)

if __name__ == "__main__":
    main()
