package org.com.mplayer.users.application.gateway.controller.follower;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.ports.in.usecase.FollowUserUsecasePort;
import org.com.mplayer.users.domain.ports.in.usecase.ListFollowersUsecasePort;
import org.com.mplayer.users.domain.ports.in.usecase.ListFollowingUsecasePort;
import org.com.mplayer.users.domain.ports.in.usecase.UnfollowUserUsecasePort;
import org.com.mplayer.users.domain.ports.out.usecase.ListFollowersOutputPort;
import org.com.mplayer.users.domain.ports.out.usecase.ListFollowingOutputPort;
import org.com.mplayer.users.domain.ports.out.utils.PageData;
import org.com.mplayer.users.infra.adapters.config.UserDetailsAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/follower")
public class FollowerController {
    private final FollowUserUsecasePort followUserUsecasePort;
    private final UnfollowUserUsecasePort unfollowUserUsecasePort;
    private final ListFollowingUsecasePort listFollowingUsecasePort;
    private final ListFollowersUsecasePort listFollowersUsecasePort;

    @PostMapping("/follow/user/{userFollowedId}")
    public ResponseEntity<Void> followUser(@AuthenticationPrincipal UserDetailsAdapter authPrincipal, @PathVariable Long userFollowedId) {
        followUserUsecasePort.execute(authPrincipal.getId(), userFollowedId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/unfollow/user/{userFollowedId}")
    public ResponseEntity<Void> unfollowUser(@AuthenticationPrincipal UserDetailsAdapter authPrincipal, @PathVariable Long userFollowedId) {
        unfollowUserUsecasePort.execute(authPrincipal.getId(), userFollowedId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<PageData<ListFollowingOutputPort>> listFollowing(
        @AuthenticationPrincipal UserDetailsAdapter authPrincipal,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
        @RequestParam(value = "name", required = false) String name
    ) {
        PageData<ListFollowingOutputPort> output = listFollowingUsecasePort.execute(authPrincipal.getId(), page, size, name);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/list/followers")
    public ResponseEntity<PageData<ListFollowersOutputPort>> listFollowers(
        @AuthenticationPrincipal UserDetailsAdapter authPrincipal,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
        @RequestParam(value = "name", required = false) String name
    ) {
        PageData<ListFollowersOutputPort> output = listFollowersUsecasePort.execute(authPrincipal.getId(), page, size, name);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
