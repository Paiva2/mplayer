package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.GetOwnCollectionOutputPort;

public interface GetOwnCollectionUsecasePort {
    GetOwnCollectionOutputPort execute(Long collectionId);
}
