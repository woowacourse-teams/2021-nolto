import React, { createContext, useState } from 'react';

import { Tech } from 'types';

interface Props {
  children: React.ReactNode;
}

interface TechTagContext {
  techs: Tech[];
  pushTech: (tech: Tech) => void;
  deleteTech: (id: number) => void;
}

export const Context = createContext<TechTagContext | null>(null);

const TechTagProvider = ({ children }: Props) => {
  const [techs, setTechs] = useState<Tech[]>([]);

  const pushTech = (tech: Tech) => {
    setTechs([...techs, tech]);
  };

  const deleteTech = (id: number) => {
    setTechs(techs.filter((tech) => tech.id !== id));
  };

  return <Context.Provider value={{ techs, pushTech, deleteTech }}>{children}</Context.Provider>;
};

export default TechTagProvider;
