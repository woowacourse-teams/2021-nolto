package com.wooteco.nolto.feed.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/feeds/{feedId:[\\d]+}/comment")
public class CommentController {
}
