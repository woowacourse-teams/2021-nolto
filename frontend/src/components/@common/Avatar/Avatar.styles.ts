import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  align-items: center;
  gap: 16px;
  width: 100%;
`;

const Image = styled.img`
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
`;

const Nickname = styled.span`
  font-size: 1.25rem;
`;

export default { Root, Image, Nickname };
