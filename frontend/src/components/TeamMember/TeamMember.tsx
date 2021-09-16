import React from 'react';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import Styled from './TeamMember.styles';

interface Props {
  name: string;
  pngUrl: string;
  webpUrl: string;
  introduction: string;
  github: string;
  site?: string;
  reverse?: boolean;
}

const TeamMember = ({ name, pngUrl, webpUrl, introduction, github, site, reverse }: Props) => {
  const MemberImage: React.ReactNode = (
    <Styled.Picture>
      <source srcSet={webpUrl} type="image/webp" />
      <source srcSet={pngUrl} type="image/png" />
      <img src={pngUrl} width="180px" alt={name} />
    </Styled.Picture>
  );

  return (
    <Styled.Root reverse={reverse}>
      {!reverse && MemberImage}

      <Styled.TextWrapper reverse={reverse}>
        <Styled.Name>{name}</Styled.Name>
        <Styled.Intro>"{introduction}"</Styled.Intro>
        <Styled.UrlContainer>
          {!reverse && <Styled.UrlBar />}
          <Styled.UrlWrapper reverse={reverse}>
            <div>
              <HighLightedText>Github</HighLightedText>
              <a href={github} target="_blank">
                {github}
              </a>
            </div>
            {site && (
              <div>
                <HighLightedText>Site</HighLightedText>
                <a href={site} target="_blank">
                  {site}
                </a>
              </div>
            )}
          </Styled.UrlWrapper>
          {reverse && <Styled.UrlBar />}
        </Styled.UrlContainer>
      </Styled.TextWrapper>
      {reverse && MemberImage}
    </Styled.Root>
  );
};

export default TeamMember;
