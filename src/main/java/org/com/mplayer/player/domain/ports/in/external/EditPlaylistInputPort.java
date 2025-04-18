package org.com.mplayer.player.domain.ports.in.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditPlaylistInputPort {
    private String name;
    private Boolean visiblePublic;
    private MultipartFile cover;
}
