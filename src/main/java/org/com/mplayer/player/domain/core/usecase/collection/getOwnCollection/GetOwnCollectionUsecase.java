package org.com.mplayer.player.domain.core.usecase.collection.getOwnCollection;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.core.usecase.common.exception.CollectionNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.GetOwnCollectionUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.CollectionDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.domain.ports.out.external.dto.GetOwnCollectionOutputPort;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class GetOwnCollectionUsecase implements GetOwnCollectionUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final CollectionDataProviderPort collectionDataProviderPort;

    @Override
    public GetOwnCollectionOutputPort execute(Long collectionId) {
        FindUserExternalProfileOutputPort user = findUser();

        Collection collection = findCollection(user.getId().toString(), collectionId);

        return mountOutput(collection);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Collection findCollection(String externalUserId, Long collectionId) {
        return collectionDataProviderPort.findByIdAndExternalUserId(externalUserId, collectionId).orElseThrow(CollectionNotFoundException::new);
    }

    private GetOwnCollectionOutputPort mountOutput(Collection collection) {
        return GetOwnCollectionOutputPort.builder()
            .id(collection.getId())
            .title(collection.getTitle())
            .artist(collection.getArtist())
            .imageUrl(collection.getImageUrl())
            .createdAt(collection.getCreatedAt())
            .musics(collection.getMusics().stream().map(GetOwnCollectionOutputPort.MusicOutput::new).toList())
            .build();
    }
}
