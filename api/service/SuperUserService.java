// ...existing code...
    @Transactional
    public List<SuperUser> getAllSuperUsers(String username) {
        User user = userRepository.findByEmail(username).orElseThrow();
        if (!permissionService.hasSuperUserPermission(user)) {
            throw new AccessDeniedException("You do not have access to view SuperUsers");
        }
        return superUserRepository.findAll();
    }

    @Transactional
    public void addSuperUser(String username, Long newUserId) {
        User user = userRepository.findByEmail(username).orElseThrow();
        if (!permissionService.hasSuperUserPermission(user)) {
            throw new AccessDeniedException("You do not have access to view SuperUsers");
        }
        // ...existing code...
    }

    @Transactional
    public void removeSuperUser(String username, Long deleteSuperId) {
        User user = userRepository.findByEmail(username).orElseThrow();
        if (!permissionService.hasSuperUserPermission(user)) {
            throw new AccessDeniedException("You do not have access to view SuperUsers");
        }
        // ...existing code...
    }
// ...existing code...
