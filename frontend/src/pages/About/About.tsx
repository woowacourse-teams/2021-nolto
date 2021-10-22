import React from 'react';
import { Helmet } from 'react-helmet-async';

import TeamMember from 'components/TeamMember/TeamMember';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import { DefaultPageRoot } from 'commonStyles';
import { FONT_SIZE } from 'constants/styles';
import member from './members';
import Styled from './About.styles';

const About = () => {
  return (
    <BaseLayout>
      <Helmet>
        <title>놀토: 팀 소개</title>
        <link rel="canonical" href="https://www.nolto.app/about" />
        <meta name="description" content="놀토 팀을 소개합니다~!" />
      </Helmet>
      <DefaultPageRoot>
        <h2>
          <HighLightedText fontSize={FONT_SIZE.X_LARGE}>We Make Nolto 🚀</HighLightedText>
        </h2>
        <Styled.MembersContainer>
          <TeamMember {...member.amazzi} />
          <TeamMember {...member.joel} reverse />
          <TeamMember {...member.pomo} />
          <TeamMember {...member.zig} reverse />
          <TeamMember {...member.mickey} />
          <TeamMember {...member.charlie} reverse />
        </Styled.MembersContainer>
      </DefaultPageRoot>
    </BaseLayout>
  );
};

export default About;
