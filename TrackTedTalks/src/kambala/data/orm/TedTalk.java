package kambala.data.orm;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

/**
 *  @author Bala subrahmanyam Kambala
 *  
 * TedTalk class which holds the details of TedTalk. This also used as data object during communication with SQLite
 */
public class TedTalk {
	
	@DatabaseField(index = true, id = true)
	private int Id;
	
	@DatabaseField
	private int EventId;
	
	@DatabaseField
	private String Name;
	
	@DatabaseField
	private String Description;
	
	@DatabaseField
	private String Slug;
	
	@DatabaseField
	private String NativeLanguage;
	
	@DatabaseField
	private Date PublishedAt;
	
	@DatabaseField
	private Date RecordedAt;
	
	@DatabaseField
	private Date UpdatedAt;
	
	@DatabaseField
	private Date ReleasedAt;
	
	public TedTalk(){
		// needed by ORM
	}
		
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Name);
		
		return sb.toString();
	}
	
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}
	
	public int getEventId() {
		return EventId;
	}
	
	public void setEventId(int eventId) {
		EventId = eventId;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	public String getDescription() {
		return Description;
	}
	
	public void setDescription(String description) {
		Description = description;
	}
	
	public String getSlug() {
		return Slug;
	}
	
	public void setSlug(String slug) {
		Slug = slug;
	}
	
	public String getNativeLanguage() {
		return NativeLanguage;
	}
	
	public void setNativeLanguage(String nativeLanguage) {
		NativeLanguage = nativeLanguage;
	}
	
	public Date getPublishedAt() {
		return PublishedAt;
	}
	
	public void setPublishedAt(Date publishedAt) {
		this.PublishedAt = publishedAt;
	}
	
	public Date getRecordedAt() {
		return RecordedAt;
	}
	
	public void setRecordedAt(Date recordedAt) {
		RecordedAt = recordedAt;
	}
	
	public Date getUpdatedAt() {
		return UpdatedAt;
	}
	
	public void setUpdatedAt(Date updatedAt) {
		UpdatedAt = updatedAt;
	}
	
	public Date getReleasedAt() {
		return ReleasedAt;
	}
	
	public void setReleasedAt(Date releasedAt) {
		ReleasedAt = releasedAt;
	}
}
