import styled from 'styled-components';

const TopContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  margin-bottom: 2rem;
  z-index: 10;
  gap: 0.5rem;
`;

const StepChipsContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 0.85rem;
`;

const Button = styled.button`
  background: transparent;
  border: none;
`;

export default {
  TopContainer,
  StepChipsContainer,
  Button,
};
