import React from 'react';

import Styled, { HighLightedText } from './TeamMember.styles';

interface Props {
  image: string;
  name: string;
  introduction: string;
  github: string;
  site?: string;
  reverse?: boolean;
}

const TeamMember = ({ image, name, introduction, github, site, reverse }: Props) => {
  return (
    <Styled.Root reverse={reverse}>
      {!reverse && <Styled.Image src={image} width="180px" />}
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
      {reverse && <Styled.Image src={image} width="180px" />}
    </Styled.Root>
  );
};

export default TeamMember;
