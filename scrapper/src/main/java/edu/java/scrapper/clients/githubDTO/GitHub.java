package edu.java.scrapper.clients.githubDTO;

public record GitHub(
    Repository repository,
    Commit[] commits,
    Pull[] pulls,
    Branch[] branches
) { }
