package assignment2;
import java.time.*;
import java.util.*;
public class SportsLeague {
    // ---------------- Player Class ----------------
    static class Player {
        private String name;
        private int age;
        private String position;
        private Team team;
        private int yellowCards;
        private int redCards;
        public Player(String name, int age, String position) {
            this.name = name;
            this.age = age;
            this.position = position;
            this.yellowCards = 0;
            this.redCards = 0;
        }
        public void assignTeam(Team team) { this.team = team; }
        public String getName() { 
              return name;
         }
        public Team getTeam() { 
                return team;
       }
        public void addYellowCard() { yellowCards++; }
        public void addRedCard() { redCards++; }
        public String toString() {
            return name + " (" + position + ") - Team: " + (team != null ? team.getName() : "None") +
                   ", Y: " + yellowCards + ", R: " + redCards;        }    }
   // ---------------- Team Class ----------------
    static class Team {
        private String name;
        private List<Player> players;
        private int matchesPlayed, wins, draws, losses, goalsFor, goalsAgainst, points;
        public Team(String name) { this.name = name; this.players = new ArrayList<>(); }
        public void addPlayer(Player player) { players.add(player); player.assignTeam(this); }
        public void updateStats(int scored, int conceded) {
            matchesPlayed++;
            goalsFor += scored;
            goalsAgainst += conceded;
            if (scored > conceded) wins++;
            else if (scored == conceded) draws++;
            else losses++;
            points = wins * 3 + draws;
        }
        public String getName() { 
                  return name; 
           }
        public int getPoints() { 
                 return points; 
          }
        public int getGoalDifference() {
                return goalsFor - goalsAgainst; 
        }
        public List<Player> getPlayers() { 
               return players; 
        }
        public String toString() { return name + " - Pts: " + points + ", GD: " + getGoalDifference(); }
    }

    // ---------------- Result Class ----------------
    static class Result {
        private int homeGoals;
        private int awayGoals;
        public Result(int homeGoals, int awayGoals) {
            this.homeGoals = homeGoals;
            this.awayGoals = awayGoals;
        }
        public int getHomeGoals() { return homeGoals; }
        public int getAwayGoals() { return awayGoals; }
    }
    // ---------------- Match Class ----------------
    static class Match {
        private Fixture fixture;
        private Result result;
        public Match(Fixture fixture) { 
                  this.fixture = fixture;
         }
        public void recordResult(Result result) {
            this.result = result;
            fixture.getHomeTeam().updateStats(result.getHomeGoals(), result.getAwayGoals());
            fixture.getAwayTeam().updateStats(result.getAwayGoals(), result.getHomeGoals());
        }
        public Fixture getFixture() { return fixture; }
        public Result getResult() { return result; }
    }
    // ---------------- Fixture Class ----------------
    static class Fixture {
        private LocalDate date;
        private LocalTime time;
        private Team homeTeam;
        private Team awayTeam;
        private Match match;
        public Fixture(LocalDate date, LocalTime time, Team home, Team away) {
            this.date = date; this.time = time;
            this.homeTeam = home; this.awayTeam = away;
            this.match = new Match(this);
        }
        public Team getHomeTeam() { 
                 return homeTeam;
            }
        public Team getAwayTeam() {  
                return awayTeam;
           }
        public Match getMatch() {
               return match; 
           }
        public LocalDate getDate() {
               return date; 
           }
        public LocalTime getTime() {
               return time; 
           }

        public String toString() {
            return date + " " + time + ": " + homeTeam.getName() + " vs " + awayTeam.getName();
        }
    }
    // ---------------- DisciplinaryRecord Class ----------------
    static class DisciplinaryRecord {
        private Player player;
        private String type;
        private Match match;
        public DisciplinaryRecord(Player player, String type, Match match) {
            this.player = player; this.type = type; this.match = match;
            if(type.equalsIgnoreCase("yellow")) player.addYellowCard();
            else if(type.equalsIgnoreCase("red")) player.addRedCard();
        }
        public String toString() {
            return player.getName() + " - " + type + " card in match: " +
                   match.getFixture().getHomeTeam().getName() + " vs " +
                   match.getFixture().getAwayTeam().getName();
        }
    }
    // ---------------- Main ----------------
    static Scanner sc = new Scanner(System.in);
    static List<Team> teams = new ArrayList<>();
    static List<Fixture> fixtures = new ArrayList<>();
    static List<Player> players = new ArrayList<>();
    static List<DisciplinaryRecord> records = new ArrayList<>();
    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n--- Sports League Menu ---");
            System.out.println("1. Add Team");
            System.out.println("2. Add Player");
            System.out.println("3. Create Fixture");
            System.out.println("4. Record Match Result");
            System.out.println("5. Add Disciplinary Action");
            System.out.println("6. View Standings");
            System.out.println("7. View Disciplinary Log");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt(); sc.nextLine();
            switch(choice) {
                case 1: addTeam(); break;
                case 2: addPlayer(); break;
                case 3: createFixture(); break;
                case 4: recordMatch(); break;
                case 5: addDisciplinary(); break;
                case 6: viewStandings(); break;
                case 7: viewDisciplinary(); break;
            }
        } while(choice != 8);
    }
    static void addTeam() {
        System.out.print("Enter Team Name: ");
        String name = sc.nextLine();
        teams.add(new Team(name));
        System.out.println("Team added.");
    }
    static void addPlayer() {
        if(teams.isEmpty()) { System.out.println("No teams available!"); return; }
        System.out.print("Enter Player Name: "); String name = sc.nextLine();
        System.out.print("Enter Age: "); int age = sc.nextInt(); sc.nextLine();
        System.out.print("Enter Position: "); String pos = sc.nextLine();
        System.out.println("Select Team:");

        for(int i=0;i<teams.size();i++) System.out.println((i+1)+". "+teams.get(i).getName());
        int t = sc.nextInt(); sc.nextLine();
        Player p = new Player(name, age, pos);
        teams.get(t-1).addPlayer(p);
        players.add(p);
        System.out.println("Player added to " + teams.get(t-1).getName());    }

       static void createFixture() {
        if(teams.size()<2) { System.out.println("Need at least 2 teams!"); return; }
        System.out.println("Select Home Team:");
        for(int i=0;i<teams.size();i++) System.out.println((i+1)+". "+teams.get(i).getName());
        int h = sc.nextInt(); sc.nextLine();
        System.out.println("Select Away Team:");
        for(int i=0;i<teams.size();i++) if(i!=h-1) System.out.println((i+1)+". "+teams.get(i).getName());
        int a = sc.nextInt(); sc.nextLine();
        System.out.print("Enter Date (YYYY-MM-DD): "); 
        LocalDate date = LocalDate.parse(sc.nextLine());
        System.out.print("Enter Time (HH:MM): "); 
        LocalTime time = LocalTime.parse(sc.nextLine());
        for(Fixture f : fixtures) {
            if(f.getHomeTeam()==teams.get(h-1) || f.getAwayTeam()==teams.get(h-1) ||
               f.getHomeTeam()==teams.get(a-1) || f.getAwayTeam()==teams.get(a-1)) {
                if(f.getDate().equals(date) && f.getTime().equals(time)) {
                    System.out.println("Team clash detected! Fixture not created."); return;
                }
            }
        }
        fixtures.add(new Fixture(date, time, teams.get(h-1), teams.get(a-1)));
        System.out.println("Fixture created.");
    }
    static void recordMatch() {
        if(fixtures.isEmpty()) { 
                  System.out.println("No fixtures scheduled."); 
                  return; 
          }
        System.out.println("Select Fixture to Record Result:");
        for(int i=0;i<fixtures.size();i++) System.out.println((i+1)+". "+fixtures.get(i));
        int f = sc.nextInt(); sc.nextLine();
        Fixture fixture = fixtures.get(f-1);
        System.out.print("Enter Home Goals: "); int hg = sc.nextInt();
        System.out.print("Enter Away Goals: "); int ag = sc.nextInt(); sc.nextLine();
        fixture.getMatch().recordResult(new Result(hg, ag));
        System.out.println("Match result recorded.");
    }
    static void addDisciplinary() {
        if(fixtures.isEmpty()) { 
           System.out.println("No fixtures available.");
           return; 
          }
        System.out.println("Select Fixture:");
        for(int i=0;i<fixtures.size();i++) 
                   System.out.println((i+1)+". "+fixtures.get(i));
        int f = sc.nextInt(); sc.nextLine();
        Fixture fixture = fixtures.get(f-1);
        System.out.println("Select Team:");
        System.out.println("1. " + fixture.getHomeTeam().getName());
        System.out.println("2. " + fixture.getAwayTeam().getName());
        int teamChoice = sc.nextInt(); sc.nextLine();
        Team team = (teamChoice==1)? fixture.getHomeTeam() : fixture.getAwayTeam();
        System.out.println("Select Player:");

        List<Player> teamPlayers = team.getPlayers();
        for(int i=0;i<teamPlayers.size();i++) System.out.println((i+1)+". "+teamPlayers.get(i).getName());
        int p = sc.nextInt(); sc.nextLine();
        Player player = teamPlayers.get(p-1);
        System.out.print("Enter Disciplinary Type (yellow/red): ");
        String type = sc.nextLine();
        records.add(new DisciplinaryRecord(player, type, fixture.getMatch()));
        System.out.println("Disciplinary record added.");
    }

    static void viewStandings() {
        teams.sort((t1,t2) -> t2.getPoints()!=t1.getPoints()?t2.getPoints()-t1.getPoints():
                                  t2.getGoalDifference()-t1.getGoalDifference());
        System.out.println("---- Standings ----");
        for(Team t : teams) System.out.println(t);
    }
    static void viewDisciplinary() {
        if(records.isEmpty()) System.out.println("No disciplinary records.");
        else for(DisciplinaryRecord r : records) System.out.println(r);
    }
}
