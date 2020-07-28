package kr.co.pickmeup.service;

import kr.co.pickmeup.domain.projects.Project;
import kr.co.pickmeup.domain.projects.ProjectRepository;
import kr.co.pickmeup.web.dto.ProjectsDto.ProjectResponseDto;
import kr.co.pickmeup.web.dto.ProjectsDto.ProjectSaveRequestDto;
import kr.co.pickmeup.web.dto.ProjectsDto.ProjectUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TagService tagService;

    // create
    public Long save(ProjectSaveRequestDto requestDto) {
        tagService.save(requestDto);

        return projectRepository.save(requestDto.toEntity()).getId();
    }

    // read one
    @Transactional
    public ProjectResponseDto findById (Long id) {
        Project entity = projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        entity.add_viewNum();

        return new ProjectResponseDto(entity);
    }

    /*
    // read all
    @Transactional(readOnly = true)
    public List<ProjectsListResponseDto> findAllDesc() {
        return projectRepository.findAllDesc().stream()
                .map(ProjectsListResponseDto::new)
                .collect(Collectors.toList());
    }
    */

    // update
    @Transactional
    public Long update(Long id, ProjectUpdateRequestDto requestDto) {
        Project project = projectRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
        project.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public void delete (Long id) {
        Project project = projectRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
        projectRepository.delete(project);
    }

    @Transactional
    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    // search
    @Transactional
    public List<ProjectResponseDto> searchProjects(String keyword) {
        List<Project> projects = new ArrayList<>();

        // keyword가 들어간 Title을 가진 애들을 찾음
        List<Project> projects_byTitle = projectRepository.findByTitleContains(keyword);

        // keyword가 들어간 Content를 가진 애들을 찾음
        List<Project> projects_byContent = projectRepository.findByContentContains(keyword);

        // merge
        projects.addAll(projects_byTitle);
        projects.addAll(projects_byContent);

        List<ProjectResponseDto> projectResponseDtoList = new ArrayList<>();

        if(projects.isEmpty()) return projectResponseDtoList;

        for(Project project : projects) {
            projectResponseDtoList.add(new ProjectResponseDto(project));
        }

        return projectResponseDtoList;
    }
}
