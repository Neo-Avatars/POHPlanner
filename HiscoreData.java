package pohplanner;

/**
 * This class manages all data for hiscore lookups, including level, xp and rank.
 * 
 * Originally written for the Skill Calcs, modified for the POH Planner.
 * 
 * @author Rick (Salmoneus), Neo Avatars
 * @version 2.1
 * @date 04 January 2009
 */

public class HiscoreData {
    int level;
    int rank;
    int xp;
    boolean isHiscoreData;
    
    public HiscoreData( int rank, int level, int xp, boolean isHiscoreData ) {
    	
        this.level = level;
        this.rank = rank;
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel( int level ) {
        this.level = level;
    }

    public int getRank() {
        return rank;
    }

    public void setRank( int rank ) {
        this.rank = rank;
    }

    public int getXp() {
        return xp;
    }

    public void setXp( int xp ) {
        this.xp = xp;
    }
    public boolean getIsHiscoreData() {
        return isHiscoreData;
    }

    public void setIsHiscoreData( boolean isHiscoreData ) {
        this.isHiscoreData = isHiscoreData;
    }
}
