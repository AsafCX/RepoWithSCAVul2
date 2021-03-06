package com.checkmarx.utils;


import com.checkmarx.controller.exception.ScmException;
import com.checkmarx.dto.IDto;
import com.checkmarx.dto.IRepoDto;
import com.checkmarx.dto.datastore.*;
import com.checkmarx.dto.web.OrgSettingsWebDto;
import com.checkmarx.dto.web.OrganizationWebDto;
import com.checkmarx.dto.web.RepoWebDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Converter {

    private Converter(){}
    
    public static OrgReposDto convertToOrgRepoDto(ScmAccessTokenDto scmAccessTokenDto, List<? extends IRepoDto> repoDtos) {
        return OrgReposDto.builder()
                .scmUrl(scmAccessTokenDto.getScmUrl())
                .orgIdentity(scmAccessTokenDto.getOrgIdentity())
                .repoList(Converter.convertListToRepoDto(repoDtos))
                .build();

    }

    public static List<RepoDto> convertListToRepoDto(List<? extends IRepoDto>  dtoList) {
        List<RepoDto> repoDtos = new ArrayList<>();
        for (IRepoDto repoDto: dtoList) {
            repoDtos.add(Converter.convertToDataStoreDto(repoDto));
        }
        return repoDtos;
    }

    public static RepoDto convertToDataStoreDto(IRepoDto repoDto) {
        return RepoDto.builder()
                .repoIdentity(repoDto.getId())
                .isWebhookConfigured(repoDto.isWebHookEnabled())
                .webhookId(repoDto.getWebhookId())
                .build();
    }
    
    public static List<OrganizationWebDto> convertToListOrgWebDtos(List<? extends IDto> dtos) {
        List<OrganizationWebDto> orgWebDtos = new ArrayList<>();
        for (IDto dto: dtos) {
            orgWebDtos.add(Converter.convertToOrgWebDto(dto));
        }
        return orgWebDtos;
    }

    public static OrganizationWebDto convertToOrgWebDto(IDto iDto) {
        return OrganizationWebDto.builder().id(iDto.getId()).name(iDto.getName()).build();
    }


    public static List<RepoWebDto> convertToListRepoWebDto(List<? extends IRepoDto>  repoDtos) {
        List<RepoWebDto> repoWebDtos = new ArrayList<>();
        for (IRepoDto repoDto : repoDtos) {
            repoWebDtos.add(Converter.convertRepoDtoToRepoWebDto(repoDto));
        }
        return repoWebDtos;
    }
    


    public static RepoWebDto convertRepoDtoToRepoWebDto(IRepoDto repoDto) {
        return RepoWebDto.builder()
                .id(repoDto.getId())
                .name(repoDto.getName())
                .webhookId(repoDto.getWebhookId())
                .webhookEnabled(repoDto.isWebHookEnabled())
                .build();
    }
    public static OrgPropertiesDto convertToCxFlowProperties(String scmUrl, String orgName,
                                                             OrgSettingsWebDto orgSettingsWebDto) {
        return OrgPropertiesDto.builder()
                .cxTeam(orgSettingsWebDto.getTeam())
                .cxGoToken(orgSettingsWebDto.getCxgoToken())
                .orgIdentity(orgName)
                .scmUrl(scmUrl)
                .build();
    }
    

    public static OrgSettingsWebDto convertOrgProToOrgSettingsWebDto(OrgPropertiesDto orgPropertiesDto) {
        String cxTeam = "";
        String cxGoToken = "";
        if (orgPropertiesDto.getCxTeam() != null){
            cxTeam = orgPropertiesDto.getCxTeam();
        }
        if (orgPropertiesDto.getCxGoToken() != null){
            cxGoToken = orgPropertiesDto.getCxGoToken();
        }
        return OrgSettingsWebDto.builder()
                .team(cxTeam)
                .cxgoToken(cxGoToken)
                .build();
    }
    

    public static List<OrgDto> convertToListOrg(String accessToken, List<?
            extends IDto> organizationDtos, String scmUrl) {
        List<OrgDto> orgDtos = new ArrayList<>();
        for (IDto orgDto: organizationDtos) {
            orgDtos.add(OrgDto.builder()
                                .orgIdentity(orgDto.getId())
                                .orgName(orgDto.getName())
                                .scmUrl(scmUrl)
                                .accessToken(accessToken)
                                .tokenType(TokenType.ACCESS.getType())
                                .build());
        }
        return orgDtos;
    }
    

}
