# Design Specification: Novas Imagens dos Botões de Seleção de Personagens

## 1. Visão Geral
Substituição das imagens dos botões de seleção de personagens (`play1.gif` a `play6.gif`) da tela de seleção (`visao.Selecao`) do jogo *Luta de Rua*, elevando o nível estético da interface para um estilo de jogo de luta arcade moderno.

## 2. Personagens e Especificações de Imagem
Cada imagem será um cartão vertical de retrato de personagem em alta qualidade:

| Arquivo | Personagem | Descrição da Arte |
| :--- | :--- | :--- |
| `play1.gif` | Chun-Li | Retrato dinâmico de Chun-Li (Street Fighter) em traje clássico azul com efeitos energéticos de combate e moldura arcade. |
| `play2.gif` | Sheeva | Retrato feroz de Sheeva (Mortal Kombat) exibindo seus 4 braços musculosos e armadura de guerreira shokan. |
| `play3.gif` | Akuma | Retrato sombrio e ameaçador de Akuma (Street Fighter) com olhos vermelhos brilhantes e aura de Satsui no Hado. |
| `play4.gif` | Cable | Retrato de Cable (Marvel) com olho cibernético brilhante em amarelo/azul e armadura pesada com brilho metálico. |
| `play5.gif` | Spider-Man | Retrato do Homem-Aranha (Marvel) em pose de combate ágil, detalhes na textura do traje e efeitos de teia ao fundo. |
| `play6.gif` | Doctor Doom | Retrato soberbo do Doutor Doom (Marvel) em máscara de ferro reluzente, capuz verde e energia mística verde nos punhos. |

## 3. Requisitos Técnicos
- **Dimensões Finais**: 110 x 244 pixels (compatível com os limites configurados em `src/modelo/CharacterData.java` e `src/visao/Selecao.java`).
- **Formato de Arquivo**: GIF (ou PNG de altíssima fidelidade mantendo a mesma referência).
- **Processamento de Imagens**: Redimensionamento e otimização automatizados via script Python (`Pillow`) para ajustar o aspect-ratio e manter a nitidez no layout Swing.
- **Localização**: `src/assets/play1.gif` até `src/assets/play6.gif`.

## 4. Plano de Teste e Validação
- Verificação visual das 6 imagens geradas.
- Compilação do projeto com Maven (`mvn test-compile` ou `mvn compile`).
- Teste de renderização do painel Swing da tela de seleção.
