package net.linaris.Practice.handlers.players;

public enum Rank
{
    PLAYER(0, "Joueur", "§7", 0, 0, '7'), 
    VIP(1, "VIP", "§d[VIP] ", 1, 0, 'd'), 
    AMI(2, "Ami", "§fAmi §b\u272a§f ", 2, 0, 'f'), 
    YT(3, "Youtubeur", "§d[YouTuber] ", 3, 0, 'd'), 
    HELPEUR(4, "Helpeur", "§3[Helpeur] ", 3, 1, '3'), 
    MOD(5, "Moderateur", "§6[Moderateur] ", 3, 2, '6'), 
    RESP(6, "Responsable", "§6[Resp.Modo] ", 3, 3, '6'), 
    ADMIN(7, "Admin", "§c[Admin] ", 3, 4, 'c'), 
    FONDATEUR(8, "Fondateur", "§c[Fondateur] ", 3, 4, 'c');  
  private int id;
  private String name;
  private String prefix;
  private int vipLevel;
  private int moderationLevel;
  private char color;
  
  private Rank(int id, String name, String prefix, int vipLevel, int moderationLevel, char color)
  {
    this.id = id;
    this.name = name;
    this.prefix = prefix;
    this.vipLevel = vipLevel;
    this.moderationLevel = moderationLevel;
    this.color = color;
  }
  
  public int getID()
  {
    return this.id;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getPrefix()
  {
    return this.prefix;
  }
  
  public int getVipLevel()
  {
    return this.vipLevel;
  }
  
  public int getModerationLevel()
  {
    return this.moderationLevel;
  }
  
  public String getColor()
  {
    return "§" + this.color;
  }
  
  public double getEloBoost()
  {
    if (this.vipLevel == 0) {
      return 0.0D;
    }
    if (this.vipLevel == 1) {
      return 2.0D;
    }
    if (this.vipLevel == 2) {
      return 3.0D;
    }
    if (this.vipLevel == 3) {
      return 4.0D;
    }
    if (this.vipLevel == 4) {
      return 4.0D;
    }
    return 0.0D;
  }
  
  public static Rank get(int i)
  {
    for (final Rank rank : values()) {
      if (rank.id == i) {
        return rank;
      }
    }
    return null;
  }
  
  public static Rank get(String name)
  {
    for (final Rank rank : values()) {
      if (rank.getName().equals(name)) {
        return rank;
      }
    }
    return null;
  }
}
