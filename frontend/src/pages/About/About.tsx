import React from 'react';

import TeamMember from 'components/TeamMember/TeamMember';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import { DefaultPageRoot } from 'commonStyles';
import { FONT_SIZE } from 'constants/styles';
import member from './members';
import Styled from './About.styles';
import HighLightedText from 'components/@common/HighlightedText/HighlightedText';

const About = () => {
  return (
    <BaseLayout>
      <DefaultPageRoot>
        <HighLightedText fontSize={FONT_SIZE.X_LARGE}>We Make Nolto 🚀</HighLightedText>
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
