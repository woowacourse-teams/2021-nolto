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
  return (
    <Styled.Root reverse={reverse}>
      <Styled.Picture>
        <source srcSet={webpUrl} type="image/webp" />
        <source srcSet={pngUrl} type="image/png" />
        <img src={pngUrl} alt={name} />
      </Styled.Picture>
      <Styled.TextWrapper reverse={reverse}>
        <Styled.Name>{name}</Styled.Name>
        <Styled.Intro>"{introduction}"</Styled.Intro>
        <Styled.UrlContainer reverse={reverse}>
          <Styled.UrlBar />
          <Styled.UrlWrapper reverse={reverse}>
            <div className="url-text">
              <HighLightedText>Github</HighLightedText>
              <a href={github} target="_blank">
                {github}
              </a>
            </div>
            {site && (
              <div className="url-text">
                <HighLightedText>Site</HighLightedText>
                <a href={site} target="_blank">
                  {site}
                </a>
              </div>
            )}
          </Styled.UrlWrapper>
        </Styled.UrlContainer>
      </Styled.TextWrapper>
    </Styled.Root>
  );
};

export default TeamMember;
