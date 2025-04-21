package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.CollectionOutput;
import org.com.mplayer.player.domain.ports.out.utils.PageData;

public interface ListOwnCollectionsUsecasePort {
    PageData<CollectionOutput> execute(int page, int size);
}
