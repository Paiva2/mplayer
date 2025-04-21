package org.com.mplayer.player.domain.core.usecase.collection.listOwnCollections;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.ports.in.usecase.ListOwnCollectionsUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.CollectionDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.CollectionOutput;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.domain.ports.out.utils.PageData;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class ListOwnCollectionsUsecase implements ListOwnCollectionsUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final CollectionDataProviderPort collectionDataProviderPort;

    @Override
    public PageData<CollectionOutput> execute(int page, int size) {
        FindUserExternalProfileOutputPort user = findUser();

        if (page < 1) {
            page = 1;
        }

        if (size < 5) {
            size = 5;
        }

        PageData<Collection> collections = findCollections(user.getId().toString(), page, size);

        return mountOutput(collections);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private PageData<Collection> findCollections(String externalUserId, int page, int size) {
        return collectionDataProviderPort.findByExternalUserId(externalUserId, page, size);
    }

    private PageData<CollectionOutput> mountOutput(PageData<Collection> collections) {
        return new PageData<>(
            collections.page(),
            collections.size(),
            collections.totalPages(),
            collections.totalItems(),
            collections.isLast(),
            collections.list().stream().map(CollectionOutput::new).toList()
        );
    }
}
