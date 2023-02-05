package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.PartOfDay;
import org.mafagafogigante.dungeon.stats.CauseOfDeath;
import org.mafagafogigante.dungeon.stats.TypeOfCauseOfDeath;
import org.mafagafogigante.dungeon.util.CounterMap;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonObject.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

class AchievementBuilder {

  private final Collection<BattleStatisticsRequirement> requirements = new ArrayList<>();
  private String id;
  private String name;
  private String info;
  private String text;
  private CounterMap<Id> killsByLocationId;
  private CounterMap<Id> visitedLocations;
  private CounterMap<Id> maximumNumberOfVisits;
  private Set<PartOfDay> partsOfDayOfDiscovery;
  private int discoveryCount;

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void addBattleStatisticsRequirement(BattleStatisticsRequirement requirement) {
    requirements.add(requirement);
  }

  public void setKillsByLocationId(CounterMap<Id> killsByLocationId) {
    if (killsByLocationId.isNotEmpty()) {
      this.killsByLocationId = killsByLocationId;
    }
  }

  public void setVisitedLocations(CounterMap<Id> visitedLocations) {
    if (visitedLocations.isNotEmpty()) {
      this.visitedLocations = visitedLocations;
    }
  }

  public void setMaximumNumberOfVisits(CounterMap<Id> maximumNumberOfVisits) {
    if (maximumNumberOfVisits.isNotEmpty()) {
      this.maximumNumberOfVisits = maximumNumberOfVisits;
    }
  }

  public void addPartOfDayOfDiscovery(PartOfDay partOfDay) {
    if (partsOfDayOfDiscovery == null) {
      partsOfDayOfDiscovery = EnumSet.noneOf(PartOfDay.class);
    }
    partsOfDayOfDiscovery.add(partOfDay);
  }

  public void setDiscoveryCount(int discoveryCount) {
    this.discoveryCount = discoveryCount;
  }

  public Achievement createAchievement() {
    return new Achievement(id, name, info, text, requirements, killsByLocationId, visitedLocations,
            maximumNumberOfVisits, partsOfDayOfDiscovery, discoveryCount);
  }
  public AchievementBuilder createAchievementBuilderByJsonValue(JsonValue achievementValue)
  {
	  JsonObject achievementObject = achievementValue.asObject();
	  this.setId(achievementObject.get("id").asString());
	  this.setName(achievementObject.get("name").asString());
	  this.setInfo(achievementObject.get("info").asString());
	  this.setText(achievementObject.get("text").asString());
      JsonValue battleRequirements = achievementObject.get("battleRequirements");
      if (battleRequirements != null) {
        for (JsonValue requirementValue : battleRequirements.asArray()) {
          
        	this.addBattleStatisticsRequirement(createBattleStatisticsRequirement(requirementValue));
        }
      }
      JsonValue explorationRequirements = achievementObject.get("explorationRequirements");
      if (explorationRequirements != null) {
        JsonValue killsByLocationId = explorationRequirements.asObject().get("killsByLocationID");
        if (killsByLocationId != null) {
        	this.setKillsByLocationId(idCounterMapFromJsonObject(killsByLocationId.asObject()));
        }
        JsonValue maximumNumberOfVisits = explorationRequirements.asObject().get("maximumNumberOfVisits");
        if (maximumNumberOfVisits != null) {
          this.setMaximumNumberOfVisits(idCounterMapFromJsonObject(maximumNumberOfVisits.asObject()));
        }
        JsonValue visitedLocations = explorationRequirements.asObject().get("visitedLocations");
        if (visitedLocations != null) {
          this.setVisitedLocations(idCounterMapFromJsonObject(visitedLocations.asObject()));
        }
        JsonValue discovery = explorationRequirements.asObject().get("discovery");
        if (discovery != null) {
          for (JsonValue value : discovery.asObject().get("parts").asArray()) {
            this.addPartOfDayOfDiscovery(PartOfDay.valueOf(value.asString()));
          }
          this.setDiscoveryCount(discovery.asObject().get("count").asInt());
        }
      }
      return this;
  } 
  
  private static CounterMap<Id> idCounterMapFromJsonObject(JsonObject jsonObject) {
	    CounterMap<Id> counterMap = new CounterMap<>();
	    for (Member member : jsonObject) {
	      counterMap.incrementCounter(new Id(member.getName()), member.getValue().asInt());
	    }
	    return counterMap;
	  }
  
  private static BattleStatisticsRequirement createBattleStatisticsRequirement(JsonValue requirementValue)
  {
	  JsonObject requirementObject = requirementValue.asObject();
      JsonObject queryObject = requirementObject.get("query").asObject();
      BattleStatisticsQuery query = new BattleStatisticsQuery();
      JsonValue idValue = queryObject.get("id");
      if (idValue != null) {
        query.setId(new Id(idValue.asString()));
      }
      JsonValue typeValue = queryObject.get("type");
      if (typeValue != null) {
        query.setType(typeValue.asString());
      }
      JsonValue causeOfDeathValue = queryObject.get("causeOfDeath");
      if (causeOfDeathValue != null) {
        JsonObject causeOfDeathObject = causeOfDeathValue.asObject();
        TypeOfCauseOfDeath type = TypeOfCauseOfDeath.valueOf(causeOfDeathObject.get("type").asString());
        Id id = new Id(causeOfDeathObject.get("id").asString());
        query.setCauseOfDeath(new CauseOfDeath(type, id));
      }
      JsonValue partOfDayValue = queryObject.get("partOfDay");
      if (partOfDayValue != null) {
        query.setPartOfDay(PartOfDay.valueOf(partOfDayValue.asString()));
      }
      int count = requirementObject.get("count").asInt();
      BattleStatisticsRequirement requirement = new BattleStatisticsRequirement(query, count);
      return requirement;
  }
}
