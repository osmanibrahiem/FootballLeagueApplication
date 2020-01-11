
package io.osman.football.league.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "teams")
public class Team {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("competition_id")
    @Expose
    private Integer competitionId;
    @Embedded
    @SerializedName("area")
    @Expose
    private Area area;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;
    @ColumnInfo(name = "shortName")
    @SerializedName("shortName")
    @Expose
    private String shortName;
    @ColumnInfo(name = "tla")
    @SerializedName("tla")
    @Expose
    private String tla;
    @ColumnInfo(name = "crestUrl")
    @SerializedName("crestUrl")
    @Expose
    private String crestUrl;
    @ColumnInfo(name = "address")
    @SerializedName("address")
    @Expose
    private String address;
    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    @Expose
    private String phone;
    @ColumnInfo(name = "website")
    @SerializedName("website")
    @Expose
    private String website;
    @ColumnInfo(name = "email")
    @SerializedName("email")
    @Expose
    private String email;
    @ColumnInfo(name = "founded")
    @SerializedName("founded")
    @Expose
    private Integer founded;
    @ColumnInfo(name = "clubColors")
    @SerializedName("clubColors")
    @Expose
    private String clubColors;
    @ColumnInfo(name = "venue")
    @SerializedName("venue")
    @Expose
    private String venue;
    @ColumnInfo(name = "lastUpdated")
    @SerializedName("lastUpdated")
    @Expose
    private String lastUpdated;
    @SerializedName("squad")
    @Expose
    private List<Player> players = null;

    public Integer getId() {
        return id;
    }

    public Integer getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Integer competitionId) {
        this.competitionId = competitionId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getTla() {
        return tla;
    }

    public void setTla(String tla) {
        this.tla = tla;
    }

    public String getCrestUrl() {
        return crestUrl;
    }

    public void setCrestUrl(String crestUrl) {
        this.crestUrl = crestUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFounded() {
        return founded;
    }

    public void setFounded(Integer founded) {
        this.founded = founded;
    }

    public String getClubColors() {
        return clubColors;
    }

    public void setClubColors(String clubColors) {
        this.clubColors = clubColors;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public static DiffUtil.ItemCallback<Team> DIFF_CALLBACK = new DiffUtil.ItemCallback<Team>() {
        @Override
        public boolean areItemsTheSame(@NonNull Team oldItem, @NonNull Team newItem) {
            return oldItem.id.intValue() == newItem.id.intValue();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Team oldItem, @NonNull Team newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;

        Team team = (Team) obj;
        return team != null && team.id.intValue() == this.id.intValue();
    }
}
