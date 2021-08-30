import React from 'react';

import TeamMember from 'components/TeamMember/TeamMember';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import { DefaultPageRoot } from 'commonStyles';
import member from './members';
import Styled from './About.styles';

const About = () => {
  return (
    <BaseLayout>
      <DefaultPageRoot>
        <Styled.SectionTitle fontSize="1.75rem">We Make Nolto 🚀</Styled.SectionTitle>
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
