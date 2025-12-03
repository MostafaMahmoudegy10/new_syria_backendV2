package freelance.new_syria_v2.article.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import freelance.new_syria_v2.auth.entity.User;
import freelance.new_syria_v2.categories.entitiy.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Enumerated(EnumType.STRING)
	private Status status;

	private LocalDate createdAt = LocalDate.now();

	private String header;

	private String bio;

	@OneToMany(mappedBy = "article")
	List<Section> sections;

	private String imageUrl;

    @Column(name = "reacts")
    private long reacts;

    @Column(name = "views")
    private long views;

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;
	
	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Reaction>reactions;
	
	@ManyToOne()
	@JoinColumn(name="category_id",nullable = false)
	Category category;


    @JsonIgnore()
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;


}
