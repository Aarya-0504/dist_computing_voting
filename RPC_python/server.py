import Pyro4

@Pyro4.expose
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

def main():
    voting_system = VotingSystem()
    daemon = Pyro4.Daemon()
    uri = daemon.register(voting_system, objectId="example.voting_system")
    print("Server URI:", uri)
    daemon.requestLoop()

if __name__ == "__main__":
    main()
