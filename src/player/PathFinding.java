package player;

import data.Territory;

import java.util.*;

public class PathFinding {
    private static Map<Territory, Path> path = new HashMap<>();

    public static void addTerritory(Territory territory) {
        PathFinding.addTerritory(territory, false);
    }

    private static void addTerritory(Territory territory, boolean lateFetch) {
        if (path.containsKey(territory)) {
            Path pa = path.get(territory);
            pa.init();
        } else {
            path.put(territory, new Path(territory, lateFetch));
        }

    }

    public static int distance(Territory from, Territory to) {
        Path path = PathFinding.path.get(from);
        Path toPath = getPath(to);
        return distance(path, toPath);

    }

    public static int distance(Path from, Path to) {
        for (int i = 0; i < from.to.size(); i++) {
            if (from.to.get(i).contains(to)) {
                return i + 1;
            }
        }
        return -1;
    }

    public static Territory getNeighbour(Territory from, Territory to, boolean sameOwner) {
        Path fromPath = getPath(from);
        Path toPath = getPath(to);
        Territory nearest = null;
        for (Path searchPath : fromPath.to.get(0)) {
            if ((sameOwner && searchPath.from.getOwner().equals(from.getOwner())) || !sameOwner) {
                if (nearest == null) {
                    nearest = searchPath.from;
                } else {
                    if (distance(searchPath, toPath) < distance(nearest, to)) {
                        nearest = searchPath.from;
                    }
                }
            }
        }
        return nearest;
    }

    public static Territory getNeighbourWithSameOwner(Territory from, Territory to) {
        return getNeighbour(from, to, true);
    }

    protected static Path getPath(Territory territory) {
        Path pa = path.get(territory);
        if (pa == null) {
            PathFinding.addTerritory(territory, true);
        }
        pa = path.get(territory);
        return pa;
    }
}

class Path {
    Territory from;
    LinkedList<Set<Path>> to;

    public Path(Territory from, boolean lateFetch) {
        this.from = from;
        to = new LinkedList<>();
        if (!lateFetch) {
            init();
        }
    }

    protected void init() {
        boolean notingNew = false;
        while (!notingNew) {
            Set<Path> find = to.peekLast();
            if (find == null) {
                find = new HashSet<>();
                find.add(this);
            }

            notingNew = true;
            Set<Path> finding = new HashSet<>();
            for (Path searchTerritory : find) {
                for (Territory newTerritory : searchTerritory.from.getNeighbors()) {
                    Path newPath = PathFinding.getPath(newTerritory);
                    boolean contains = false;
                    for (Set<Path> previousFinings : to) {
                        if (previousFinings.contains(newPath)) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        finding.add(newPath);
                    }
                }
            }
            if (finding.size() != 0) {
                notingNew = false;
                to.add(finding);
            }
        }
    }
}