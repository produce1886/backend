package kr.co.pickmeup.web.dto.ProjectsDto;

import kr.co.pickmeup.domain.projects.Projects;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProjectsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private Integer viewNum;
    private LocalDateTime modifiedDate;

    public ProjectsListResponseDto(Projects entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.viewNum = entity.getViewNum();
        this.modifiedDate = entity.getModifiedDate();
    }

}
