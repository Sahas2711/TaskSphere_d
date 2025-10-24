package com.mit.tasksphere.UserService.Services.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mit.tasksphere.UserService.Entities.Team;
import com.mit.tasksphere.UserService.Entities.TeamMember;
import com.mit.tasksphere.UserService.Entities.User;
import com.mit.tasksphere.UserService.Repository.TeamRepository;
import com.mit.tasksphere.UserService.Repository.UserRepository;
import com.mit.tasksphere.UserService.Services.TeamService;

@Service
public class TeamServiceImpl implements TeamService{

	@Autowired
	private TeamRepository teamRepo;
    
    @Autowired
    private UserRepository userRepo;

	private static final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class); 

	@Override
	public void createTeam(Team team, String creatorEmail) {
	    logger.info("{} started team creation.", creatorEmail);
	    team.setCreated_by(creatorEmail);

	    // Fetch the creator user from DB
	    User creator = userRepo.findByEmail(creatorEmail)
	            .orElseThrow(() -> new RuntimeException("User not found with email: " + creatorEmail));

	    // Create a TeamMember entity for the creator with OWNER role
	    TeamMember creatorMember = new TeamMember();
	    creatorMember.setTeam(team);
	    creatorMember.setUser(creator);
	    creatorMember.setRole("OWNER");

	    // Initialize members list if null and add the creator
	    if (team.getMembers() == null) {
	        team.setMembers(new ArrayList<>());
	    }
	    team.getMembers().add(creatorMember);

	    // Save team (cascade will automatically save TeamMember)
	    teamRepo.save(team);

	    logger.info("Team created successfully with ID: {}", team.getId());
	}


	@Override
	public List<Team> getAllTeams() {
		return teamRepo.findAll();
	}
    
    @Override
    public Optional<Team> getTeamById(Long teamId) {
        return teamRepo.findById(teamId);
    }

    @Override
    public Team addMemberToTeam(Long teamId, String memberEmail) {
        Team team = teamRepo.findById(teamId)
                            .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));
        
        User member = userRepo.findByEmail(memberEmail)
                              .orElseThrow(() -> new RuntimeException("User not found with email: " + memberEmail));

        logger.info("Adding member {} to team {}", memberEmail, teamId);
        
        // Ensure the members list is initialized
        if (team.getMembers() == null) {
            team.setMembers(new ArrayList<>());
        }
        
        // Add member if not already present
        if (!team.getMembers().contains(member)) {
        	String creatorEmail = teamRepo.getTeamById(teamId).get().getCreated_by();
        	User user = userRepo.findByEmail(creatorEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        		TeamMember member1 = new TeamMember();
        		member1.setTeam(team);
        		member1.setUser(user);
        		member1.setRole("OWNER"); 

				if (team.getMembers() == null) {
				    team.setMembers(new ArrayList<>());
				}
				team.getMembers().add(member1);

            return teamRepo.save(team);
        } else {
            logger.warn("Member {} is already in team {}", memberEmail, teamId);
            return team;
        }
    }

    @Override
    public List<TeamMember> getAllTeamMembers(Long teamId) {
        Team team = teamRepo.findById(teamId)
                            .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));
        
        // Return members, handling null if the collection was never initialized (though JPA usually does this)
        return team.getMembers() != null ? team.getMembers() : new ArrayList<>();
    }

    @Override
    public Team updateTeamMember(Long teamId, String memberEmail, String newRole) {
        // --- SIMULATED BUSINESS LOGIC ---
        logger.info("Attempting to update role for member {} in team {} to {}", memberEmail, teamId, newRole);
        
        Team team = teamRepo.findById(teamId)
                            .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));
        
        // As the current Team entity lacks a specific TeamMember object for roles, 
        // this method remains a placeholder indicating where complex role logic would go.
        // It currently only validates team existence.
        userRepo.findByEmail(memberEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + memberEmail));

        logger.info("Successfully SIMULATED update of member details for {} in team {}", memberEmail, teamId);
        return team;
    }

    @Override
    public void deleteTeamMember(Long teamId, String memberEmail) {
        Team team = teamRepo.findById(teamId)
                            .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));
        
        User memberToRemove = userRepo.findByEmail(memberEmail)
                                      .orElseThrow(() -> new RuntimeException("User not found with email: " + memberEmail));
        
        if (team.getMembers() == null) {
             throw new RuntimeException("Team has no members to delete.");
        }
        
        if (team.getMembers().remove(memberToRemove)) {
            teamRepo.save(team);
            logger.info("Successfully removed member {} from team {}", memberEmail, teamId);
        } else {
            logger.warn("Member {} was not found in team {}", memberEmail, teamId);
            throw new RuntimeException("Member not found in the team.");
        }
    }
}