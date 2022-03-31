package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Tag> readAllTags(@RequestParam Map<String, String> paginationParameters) {
        return tagService.findAllTags(paginationParameters);
    }

    @GetMapping("/widely-used-tag")
    @ResponseStatus(OK)
    public Tag readWidelyUsedTag() {
        return tagService.findWidelyUsedTag();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.addTag(tag);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Tag readTag(@PathVariable long id) {
        return tagService.findTagById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public Tag updateTag(@RequestBody Tag tag, @PathVariable long id) {
        tag.setId(id);
        return tagService.updateTag(tag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteTag(@PathVariable long id) {
        tagService.removeTagById(id);
    }
}
