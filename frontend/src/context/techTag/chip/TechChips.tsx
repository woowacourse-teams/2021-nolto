import React from 'react';

import { ButtonStyle } from 'types';
import useTechTag from '../useTechTag';
import Styled, { TechButton } from './TechChips.styles';

interface Props {
  className?: string;
  reverse?: boolean;
}

const TechChips = ({ className, reverse = false }: Props) => {
  const techTag = useTechTag();

  const deleteTech = (techId: number) => {
    techTag.deleteTech(techId);
  };

  return (
    <Styled.Root className={className}>
      {techTag.techs.map((tech) => (
        <TechButton
          buttonStyle={ButtonStyle.SOLID}
          key={tech.id}
          reverse={reverse}
          onClick={() => deleteTech(tech.id)}
        >
          {tech.text}
        </TechButton>
      ))}
    </Styled.Root>
  );
};

export default TechChips;
